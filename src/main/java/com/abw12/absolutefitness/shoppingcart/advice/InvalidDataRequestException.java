package com.abw12.absolutefitness.shoppingcart.advice;

public class InvalidDataRequestException extends RuntimeException{
    public InvalidDataRequestException(String errorMsg ){
        super(errorMsg);
    }

}
