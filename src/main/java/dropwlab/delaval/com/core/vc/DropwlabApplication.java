package dropwlab.delaval.com;

import dropwlab.delaval.com.core.vc.EventService;
import dropwlab.delaval.com.health.TemplateHealthCheck;
import dropwlab.delaval.com.resources.HelloFarmerResource;
import dropwlab.delaval.com.resources.VCResource;
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

        final HelloFarmerResource farmerResource = new HelloFarmerResource(
                configuration.getTemplate(),
                configuration.getDefaultName(),
                new AtomicLong()
        );

        final VCResource vcResource = new VCResource(new AtomicLong(), new EventService());

        final TemplateHealthCheck healthCheck =
                new TemplateHealthCheck(configuration.getTemplate());
        environment.healthChecks().register("template", healthCheck);

        environment.jersey().register(farmerResource);
        environment.jersey().register(vcResource);
    }

}
