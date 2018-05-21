/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bricks.exception;

/**
 *
 * @author gouri
 */
public class BadRequestException extends RuntimeException {
    public BadRequestException(String exception) {
        super(exception);
    }
}
