package com.main;

import java.util.Vector;

public interface ReadFileApi {

	public void analyze();

	public Vector<String> getTools();

	public String getCoord();

	public void outputFile(String[] strings, String filePath);
}
