package com.mom.storefront_panoply;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class HelloController {

    @GetMapping("/lefta")
    public ResponseEntity<String> lefta() {
        return ResponseEntity.ok("Θα βγάλουμε λεφτά ρε.. υπομονή.. 18:22");
    }

    @GetMapping("")
    public ResponseEntity<String> test (){
        return ResponseEntity.ok("Υπομονή και όνειρα..");
    }
}
