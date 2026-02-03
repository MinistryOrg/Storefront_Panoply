package com.mom.storefront_panoply.tlr;

import com.mom.storefront_panoply.games.model.Game;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class HelloController {

    @GetMapping("/lefta")
    public ResponseEntity<String> lefta() {
        return ResponseEntity.ok("Tha vgaloume lefta re : 9:09");
    }

    @GetMapping("")
    public ResponseEntity<String> test (){
        return ResponseEntity.ok("Υπομονή και όνειρα..");
    }
}
