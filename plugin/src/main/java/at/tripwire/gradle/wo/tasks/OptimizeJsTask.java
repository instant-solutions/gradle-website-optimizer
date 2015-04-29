package at.tripwire.gradle.wo.tasks;

import at.tripwire.gradle.wo.optimizer.JsProcessor;
import org.gradle.api.tasks.TaskAction;

public class OptimizeJsTask extends BaseOptimizeTask {

    @TaskAction
    public void optimize() {
        super.optimize(new JsProcessor());
    }
}
