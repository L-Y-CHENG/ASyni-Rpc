package com.evoluc.asyni;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TestParams {

    private String testString;

    private int testInt;
}
