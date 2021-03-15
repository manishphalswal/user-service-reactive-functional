package org.ipo.rwa.reactive.userservice;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

import org.ipo.rwa.reactive.userservice.controller.UserController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@SpringBootApplication
public class UserServiceReactiveAnnotatedApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserServiceReactiveAnnotatedApplication.class, args);
	}

	@Bean
	public RouterFunction<ServerResponse> routes(final UserController userController) {
		return RouterFunctions
				.route(GET("/users")
								.and(accept(MediaType.APPLICATION_JSON)),
						serverRequest -> userController.getUsers())
				.andRoute(GET("/user/{id}")
								.and(accept(MediaType.APPLICATION_JSON)),
						userController::getUser)
				.andRoute(POST("/saveUser")
								.and(contentType(MediaType.APPLICATION_JSON))
								.and(accept(MediaType.APPLICATION_JSON)),
						userController::saveUser)
				.andRoute(PUT("/updateUser")
								.and(contentType(MediaType.APPLICATION_JSON))
								.and(accept(MediaType.APPLICATION_JSON)),
						userController::updateUser)
				.andRoute(DELETE("/deleteUser/{id}")
								.and(accept(MediaType.APPLICATION_JSON)),
						userController::deleteUser);
	}

}
