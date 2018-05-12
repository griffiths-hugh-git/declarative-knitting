package com.griffiths.hugh.ui.util;

import java.io.IOException;

public class ImageNotFoundException extends IOException {
	public ImageNotFoundException(String message) {
		super(message);
	}

	public ImageNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}
