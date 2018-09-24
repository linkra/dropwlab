package dropwlab.delaval.com;

import dropwlab.delaval.com.health.TemplateHealthCheck;
import dropwlab.delaval.com.resources.HelloFarmerResource;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import java.util.concurrent.atomic.AtomicLong;

/** parameterized with the application’s configuration type*/
public class DropwlabApplication extends Application<DropwlabConfiguration> {

    /** application’s entry point*/
    public static void main(final String[] args) throws Exception {
        new DropwlabApplication().run(args);
    }

    @Override
    public String getName() {
        return "Dropwlab";
    }

    @Override
    public void initialize(final Bootstrap<DropwlabConfiguration> bootstrap) {
        // TODO: application initialization
    }

    /** Environment acts like a registry of all the things your application can do*/
    @Override
    public void run(final DropwlabConfiguration configuration,
                    final Environment environment) {

        final HelloFarmerResource resource = new HelloFarmerResource(
                configuration.getTemplate(),
                configuration.getDefaultName(),
                new AtomicLong()
        );

        final TemplateHealthCheck healthCheck =
                new TemplateHealthCheck(configuration.getTemplate());
        environment.healthChecks().register("template", healthCheck);

        environment.jersey().register(resource);
    }

}
