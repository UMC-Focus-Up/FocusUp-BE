package com.focusup.global.handler.annotation;

import io.swagger.v3.oas.annotations.Parameter;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@Parameter(hidden = true)
public @interface Auth {
}
