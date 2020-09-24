package com.activiti.helper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Triple<K,V,T> {

    private K key;
    private V value;
    private T type;
}
