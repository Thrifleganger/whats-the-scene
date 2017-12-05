package com.thrifleganger.alexa.scene;

import me.ccampo.spring.aws.lambda.SpringRequestStreamHandler;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class MainHandler extends SpringRequestStreamHandler {

    //private static final ApplicationContext context =
    //       new AnnotationConfigApplicationContext(BeanConfiguration.class);

    @Override
    public ApplicationContext getApplicationContext() {
        Application application = new Application();
        application.run();
        return application.getApplicationContext();
    }
}
