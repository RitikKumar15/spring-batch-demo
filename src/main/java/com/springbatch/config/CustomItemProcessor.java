package com.springbatch.config;

import com.springbatch.entity.CoffeeEntity;
import org.springframework.batch.item.ItemProcessor;

import java.util.UUID;

public class CustomItemProcessor implements ItemProcessor<CoffeeEntity, CoffeeEntity> {

    @Override
    public CoffeeEntity process(final CoffeeEntity item) {

        String uuid = UUID.randomUUID().toString();
        String transformedBrand = item.getBrand().toUpperCase();
        String transformedOrigin = item.getOrigin().toUpperCase();
        String transformedCharacteristics = item.getCharacteristics().toUpperCase();

        return CoffeeEntity.builder().brand(transformedBrand).coffeeId(uuid)
                .origin(transformedOrigin)
                .characteristics(transformedCharacteristics)
                .build();
    }
}
