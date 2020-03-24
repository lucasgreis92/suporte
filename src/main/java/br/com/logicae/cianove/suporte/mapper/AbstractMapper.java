/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.logicae.cianove.suporte.mapper;

/**
 *
 * @author Lucas
 */
public abstract class AbstractMapper<T,O> {
    
    public abstract T convertToModel(O dto, T model);
    
    public abstract O convertToDto(T model);
}
