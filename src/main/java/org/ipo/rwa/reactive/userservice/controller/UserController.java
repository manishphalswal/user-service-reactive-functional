package org.ipo.rwa.reactive.userservice.controller;

import org.ipo.rwa.reactive.userservice.bean.UserBean;
import org.ipo.rwa.reactive.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(final UserService userService) {
        super();
        this.userService = userService;
    }

    public Mono<ServerResponse> getUsers() {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(this.userService.getUsers(), UserBean.class);
    }

    public Mono<ServerResponse> getUser(final ServerRequest serverRequest) {

        String id = serverRequest.pathVariable("id");
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(this.userService.getUserById(id), UserBean.class)
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> saveUser(final ServerRequest serverRequest) {
        Mono<UserBean> userBeanMono = serverRequest.bodyToMono(UserBean.class);
        return userBeanMono.flatMap(redeemedUser -> ServerResponse
                    .status(HttpStatus.CREATED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(this.userService.saveUser(redeemedUser), UserBean.class)
                    .switchIfEmpty(ServerResponse.notFound().build())
        );

    }

    public Mono<ServerResponse> updateUser(final ServerRequest serverRequest) {
        Mono<UserBean> userBeanMono = serverRequest.bodyToMono(UserBean.class);

        return userBeanMono.flatMap(redeemedUser -> {
            return this.userService.getUserById(redeemedUser.getId())
                    .flatMap(existingUser ->
                            ServerResponse
                                    .status(HttpStatus.CREATED)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .body(this.userService.updateUser(redeemedUser), UserBean.class)
                                    .switchIfEmpty(ServerResponse.notFound().build()));}
        );
    }

    public Mono<ServerResponse> deleteUser(final ServerRequest serverRequest) {

        String id = serverRequest.pathVariable("id");
        return this.userService.getUserById(id)
                .flatMap(redeemedUser -> ServerResponse.ok()
                        .build(this.userService.deleteUserById(id)))
                .switchIfEmpty(ServerResponse.notFound().build());
    }
}
