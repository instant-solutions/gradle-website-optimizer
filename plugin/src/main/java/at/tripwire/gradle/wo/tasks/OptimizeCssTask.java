package at.tripwire.gradle.wo.tasks;

import at.tripwire.gradle.wo.optimizer.CssProcessor;
import org.gradle.api.tasks.TaskAction;

public class OptimizeCssTask extends BaseOptimizeTask {

    @TaskAction
    public void optimize() {
        super.optimize(new CssProcessor());
    }
}
