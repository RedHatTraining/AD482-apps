package com.redhat.energy.api;

import java.util.concurrent.CompletableFuture;

public class WindAPIAdapter {

    public CompletableFuture<Void> post(WindSpeed entity) throws InterruptedException {

        Thread.sleep(2000);

        return CompletableFuture.supplyAsync(() -> {
            System.out.println("\n\nAPI Response");

            return null;
        });
    }

}
