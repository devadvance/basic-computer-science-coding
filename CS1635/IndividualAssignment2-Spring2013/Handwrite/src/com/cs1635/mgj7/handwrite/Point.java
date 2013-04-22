package com.cs1635.mgj7.handwrite;

import java.io.Serializable;

class Point implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//float x, y;
	public int x, y;
	
    @Override
    public String toString() {
        return x + ", " + y;
    }
}