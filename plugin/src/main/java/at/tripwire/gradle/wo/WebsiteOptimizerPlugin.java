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
                HtmlParser.Builder parserBuilder = new HtmlParser.Builder();

                for (File file : projectContainer.getSrcFiles()) {
                    parserBuilder.add(file);
                }

                try {
                    List<HtmlFile> htmlFiles = parserBuilder.build().parse();

                    for (HtmlFile htmlFile : htmlFiles) {
                        for (OptimizeTag optimizeTag : htmlFile.getOptimizeTags()) {
                            addOptimizeTask(project, optimizeTag);
                        }

                        // TODO: create HtmlTask and add optimize tasks as dependency
                    }

                } catch (ParseException e) {
                    e.printStackTrace(); // TODO: implement
                }
            }
        });
    }

    private void addOptimizeTask(Project project, OptimizeTag tag) {
        BaseOptimizeTask baseOptimizeTask = null;
        String name;
        String tagName = tag.getSrcTag().getName();

        File destFile = Utils.getDestinationFile(project, tag.getSrcTag());

        switch (tag.getSrcTag().getFileType()) {
            case JS:
                name = "optimizeJs-" + tagName;
                baseOptimizeTask = project.getTasks().create(name, OptimizeJsTask.class);
                break;
            case CSS:
                name = "optimizeCss-" + tagName;
                baseOptimizeTask = project.getTasks().create(name, OptimizeCssTask.class);
                break;
        }

        baseOptimizeTask.setSrcFiles(tag.getSrcFiles());
        baseOptimizeTask.setDestFile(destFile);
    }
}
