package com.project.reddit.service.likedislike;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class SimpleLikeOrDislikeFactory{

   private final List<ILikeOrDislike> services;

   public ILikeOrDislike getImplementation(Class<?> tClass) {
       var find = services.stream()
               .filter(e -> tClass.isInstance(e)).findAny();

       return find.get();
   }

}
