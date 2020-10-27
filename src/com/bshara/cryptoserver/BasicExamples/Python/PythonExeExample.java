package com.bshara.cryptoserver.BasicExamples.Python;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;

public class PythonExeExample {

	public static void main(String[] args) throws IOException, InterruptedException {
		String filePath = "calc.py";
		File f = new File(filePath);

		if (f.exists() && !f.isDirectory()) {
			System.out.println("file: " + filePath + " exists");
		} else {
			System.out.println("file: " + filePath + " doesn't exists");
		}

		String command = "cmd /c python " + filePath + " 1 2 3";

		Process p = Runtime.getRuntime().exec("C:\\Program Files (x86)\\Microsoft Visual Studio\\Shared\\Python36_64.exe" , new String[] {command});
		p.waitFor();
		BufferedReader bri = new BufferedReader(new InputStreamReader(p.getInputStream()));
		BufferedReader bre = new BufferedReader(new InputStreamReader(p.getErrorStream()));
		String line;
		while ((line = bri.readLine()) != null) {
			System.out.println(line);
		}
		bri.close();
		while ((line = bre.readLine()) != null) {
			System.out.println(line);
		}
		bre.close();
		p.waitFor();
		System.out.println("Done.");

		p.destroy();
	}
}
