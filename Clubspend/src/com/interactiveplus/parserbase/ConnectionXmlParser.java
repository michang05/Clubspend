package com.interactiveplus.parserbase;

import java.io.InputStream;
import java.util.List;

public interface ConnectionXmlParser {

	List<?> parse(InputStream inputStream);
	List<?> parse();
}
