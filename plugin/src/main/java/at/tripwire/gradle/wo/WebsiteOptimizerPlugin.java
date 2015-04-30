package at.tripwire.gradle.wo;

import at.tripwire.gradle.wo.exceptions.ParseException;
import at.tripwire.gradle.wo.tags.OptimizeTag;
import at.tripwire.gradle.wo.tasks.BaseOptimizeTask;
import at.tripwire.gradle.wo.tasks.OptimizeCssTask;
import at.tripwire.gradle.wo.tasks.OptimizeJsTask;
import org.gradle.api.Action;
import org.gradle.api.Plugin;
import org.gradle.api.Project;

import java.io.File;
import java.util.List;

public class WebsiteOptimizerPlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {
        final WebsiteProject projectContainer = new WebsiteProject();

        project.getExtensions().add("website", projectContainer);

        project.afterEvaluate(new Action<Project>() {
            @Override
            public void execute(Project project) {
                File jsDestFolder = getJsDestination(projectContainer);
                File cssDestFolder = getCssDestination(projectContainer);

                HtmlParser.Builder parserBuilder = new HtmlParser.Builder();

                for (File file : projectContainer.getSrcFiles()) {
                    parserBuilder.add(file);
                }

                try {
                    List<HtmlFile> htmlFiles = parserBuilder.build().parse();

                    for (HtmlFile htmlFile : htmlFiles) {
                        for (OptimizeTag optimizeTag : htmlFile.getOptimizeTags()) {
                            addOptimizeTask(project, optimizeTag, jsDestFolder, cssDestFolder);
                        }


                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private File getJsDestination(WebsiteProject projectContainer) {
        return projectContainer.getJsDest() != null ? projectContainer.getJsDest() : new File(".");
    }

    private File getCssDestination(WebsiteProject projectContainer) {
        return projectContainer.getCssDest() != null ? projectContainer.getCssDest() : new File(".");
    }

    private void addOptimizeTask(Project project, OptimizeTag tag, File jsDestFolder, File cssDestFolder) {
        BaseOptimizeTask baseOptimizeTask = null;
        String name;
        String tagName = tag.getSrcTag().getName();

        File destFile = null;

        switch (tag.getSrcTag().getFileType()) {
            case JS:
                name = "optimizeJs-" + tagName;
                baseOptimizeTask = project.getTasks().create(name, OptimizeJsTask.class);
                destFile = new File(jsDestFolder, tagName + ".min.js");
                break;
            case CSS:
                name = "optimizeCss-" + tag.getSrcTag().getName();
                baseOptimizeTask = project.getTasks().create(name, OptimizeCssTask.class);
                destFile = new File(cssDestFolder, tagName + ".min.css");
                break;
        }

        baseOptimizeTask.setSrcFiles(tag.getSrcFiles());
        baseOptimizeTask.setDestFile(destFile);
    }
}
