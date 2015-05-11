package at.tripwire.gradle.wo;

import at.tripwire.gradle.wo.exceptions.ParseException;
import at.tripwire.gradle.wo.tags.OptimizeTag;
import at.tripwire.gradle.wo.tasks.BaseOptimizeTask;
import at.tripwire.gradle.wo.tasks.OptimizeCssTask;
import at.tripwire.gradle.wo.tasks.OptimizeHtmlTask;
import at.tripwire.gradle.wo.tasks.OptimizeJsTask;
import com.google.common.base.CaseFormat;
import org.gradle.api.*;

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

                    Task optimizeWebsiteTask = project.getTasks().create("optimizeWebsite");

                    for (HtmlFile htmlFile : htmlFiles) {
                        OptimizeHtmlTask htmlTask = addHtmlTask(project, htmlFile, optimizeWebsiteTask, projectContainer);

                        for (OptimizeTag optimizeTag : htmlFile.getOptimizeTags()) {
                            addOptimizeTask(project, optimizeTag, htmlTask);
                        }
                    }

                } catch (ParseException e) {
                    throw new GradleScriptException("Failed to parse HTML file!", e);
                }
            }
        });
    }

    private OptimizeHtmlTask addHtmlTask(Project project, HtmlFile htmlFile, Task optimizeWebsiteTask, WebsiteProject container) {
        OptimizeHtmlTask task = project.getTasks().create("optimizeHtml-" + htmlFile.getSrcFile().getName(), OptimizeHtmlTask.class);
        optimizeWebsiteTask.dependsOn(task);
        task.setSrcFile(htmlFile);
        task.setDestFile(Utils.getDestinationFile(project, htmlFile));
        task.setOptions(container.getHtmlOptions());

        return task;
    }

    private void addOptimizeTask(Project project, OptimizeTag tag, Task htmlTask) {
        BaseOptimizeTask baseOptimizeTask = null;
        String name;
        String tagName = tag.getSrcTag().getName();
        tagName = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, tagName);

        File destFile = Utils.getDestinationFile(project, tag.getSrcTag());

        switch (tag.getSrcTag().getFileType()) {
            case JS:
                name = "optimizeJs" + tagName;
                baseOptimizeTask = project.getTasks().create(name, OptimizeJsTask.class);
                break;
            case CSS:
                name = "optimizeCss" + tagName;
                baseOptimizeTask = project.getTasks().create(name, OptimizeCssTask.class);
                break;
        }

        baseOptimizeTask.setSrcFiles(tag.getSrcFiles());
        baseOptimizeTask.setDestFile(destFile);
        htmlTask.dependsOn(baseOptimizeTask);
    }
}
