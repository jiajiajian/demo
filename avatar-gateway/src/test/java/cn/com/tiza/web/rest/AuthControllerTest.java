package cn.com.tiza.web.rest;

import org.junit.Test;
import reactor.core.publisher.Mono;

import static org.junit.Assert.*;

public class AuthControllerTest {

    @Test
    public void testMono() {
        Mono.empty().subscribe(t -> {
            assertNull(t);
           System.out.println(t);
        }, error-> {
            System.out.println(error);
                },
                () -> {
            System.out.println("complete!");
        });
        Mono.empty().map(t -> {
            System.out.println("empty");
            return t;
        });

        Mono.justOrEmpty("t").subscribe(t -> {
            System.out.println(t);
        });
    }
}