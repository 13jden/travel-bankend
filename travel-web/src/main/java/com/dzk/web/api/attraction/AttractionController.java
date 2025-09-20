package com.dzk.web.api.attraction;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AttractionController {

    @Autowired
    AttractionService attractionService;



}
