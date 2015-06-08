package com.loginjones.examples;

import java.io.IOException;
import java.text.ParseException;

public class Parse {

	public static void main(String[] args) throws IOException, ParseException {
		
		RbcVisaPdfStatementParser parser=new RbcVisaPdfStatementParser();
		parser.parse();

	}

}
