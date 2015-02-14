import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;

public class SingleFold {
	String strFoldID = null;
	ArrayList<String> fnameTrainPositive;
	ArrayList<String> fnameTrainNegetive;
	double dPositiveProbability = 0.0; //P(C=Yes)
	double dNegetiveProbability = 0.0; //P(C=No)

	ArrayList<String> fnameTestPositive;
	int iTotTestPositiveCorrect = 0;
	ArrayList<String> fnameTestNegetive;
	int iTotTestNegetiveCorrect = 0;


	HashMap<String, DocumentBean> trainPositive;
	int iTotTrainPositive = 0;
	HashMap<String, DocumentBean> trainNegetive;
	int iTotTrainNegetive = 0;

	HashMap<String, DocumentBean> testPositive;
	double dTestPositiveSmoothing = 0.0;
	HashMap<String, DocumentBean> testNegetive;
	double dTestNegetiveSmoothing = 0.0;
	double arg1 = 0.0, arg2 = 0.0;

	HashMap<String, DocumentBean> uniqueWords;
	int iTotUniqueWords = 0;
	double unknWordProbPositive = 0.0;
	double unknWordProbNegetive = 0.0;

	public SingleFold(String foldID, ArrayList<String> arrTrainPositive,
			ArrayList<String> arrTrainNegetive,
			ArrayList<String> arrTestPositive, ArrayList<String> arrTestNegetive) {
		// TODO Auto-generated constructor stub

		strFoldID = foldID;
		fnameTrainPositive = arrTrainPositive;
		fnameTrainNegetive = arrTrainNegetive;
		fnameTestPositive = arrTestPositive;
		fnameTestNegetive = arrTestNegetive;
		
		dPositiveProbability = (double) arrTrainPositive.size() / (double)(arrTrainPositive.size() + arrTrainNegetive.size());
		dNegetiveProbability = (double) arrTrainNegetive.size() / (double)(arrTrainPositive.size() + arrTrainNegetive.size());
		
		
		
		uniqueWords = new HashMap<String, DocumentBean>();
		trainPositive = new HashMap<String, DocumentBean>();
		trainNegetive = new HashMap<String, DocumentBean>();
		testPositive = new HashMap<String, DocumentBean>();
		testNegetive = new HashMap<String, DocumentBean>();

//		System.out.println("Calculating Fold " + strFoldID);

		readInputData();

	}

	private void readInputData() {
		for (String path : fnameTrainPositive) {
			readIntoPositiveHashMap(path);
		}
		for (String path : fnameTrainNegetive) {
			readIntoNegetiveHashMap(path);
		}
		iTotUniqueWords = uniqueWords.keySet().size();
//		System.out.println("iTotUniqueWords: " + iTotUniqueWords);
		unknWordProbPositive = (double) 1
				/ (double) (iTotTrainPositive + iTotUniqueWords); // Add one
																	// smoothed
																	// value
		unknWordProbNegetive = (double) 1
				/ (double) (iTotTrainNegetive + iTotUniqueWords); // Add one
																	// smoothed
																	// value

		// Calculate thier probabilities
		calculateBayesianProbabilities();
		System.out.println("Generated the model file.");
	}
	

	private void calculateBayesianProbabilities() {
		File file = new File(new File("").getAbsoluteFile() + "/Positive_Model"+ ".txt");
		// System.out.println("Generating model file at " + lmPath + " .....");
		File file2 = new File(new File("").getAbsoluteFile() + "/Negetive_Model"+ ".txt");

		OutputStream os;
		try {
			os = new FileOutputStream(new File("").getAbsoluteFile() + "/Positive_Model"+ ".txt");
			OutputStreamWriter osr = new OutputStreamWriter(os);

			if (!file.exists()) {
				file.createNewFile();
			}

			BufferedWriter bw = new BufferedWriter(osr);
//			bw.write("Positive:\n");
			double probability = 0.0;
			for (String key : trainPositive.keySet()) {
				probability = (double) (trainPositive.get(key).getCount() + 1)
						/ (double) (iTotTrainPositive + iTotUniqueWords);
				trainPositive.get(key).setProbability(probability);
				bw.write(key + "\t"
						+ (double)Math.log((double) trainPositive.get(key).getProbability())/(double)Math.log(2)
						+ "\n");
			}//(double) trainPositive.get(key).getProbability()
			bw.close();
			double dPositiveSmoothing =(double)(1/(double)(iTotTrainPositive + iTotUniqueWords));
			System.out.println("Positive Smoothing:"+(double)Math.log(dPositiveSmoothing)/(double)Math.log(2));
			
			os = new FileOutputStream(new File("").getAbsoluteFile() + "/Negetive_Model"+ ".txt");
			osr = new OutputStreamWriter(os);
			bw = new BufferedWriter(osr);

			if (!file2.exists()) {
				file2.createNewFile();
			}
//			bw.write("Negetive:\n");
			probability = 0.0;
			for (String key : trainNegetive.keySet()) {
				probability = (double) (trainNegetive.get(key).getCount() + 1)
						/ (double) (iTotTrainNegetive + iTotUniqueWords);
				trainNegetive.get(key).setProbability(probability);
				bw.write(key + "\t"
						+ (double)Math.log((double) trainNegetive.get(key).getProbability())/(double)Math.log(2)
						+ "\n");
			}
			bw.close();//(double) trainNegetive.get(key).getProbability()
			double dNegetiveSmoothing =(double)(1/(double)(iTotTrainNegetive + iTotUniqueWords));
			System.out.println("Negetive Smoothing:"+(double)Math.log(dNegetiveSmoothing)/(double)Math.log(2));
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void readIntoPositiveHashMap(String filePath) {
		InputStream is = null;
		InputStreamReader isr = null;
		BufferedReader br = null;

		try {
			is = new FileInputStream(filePath);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		isr = new InputStreamReader(is);

		String[] words = null;

		File file = new File(filePath);
		if (file.exists()) {
			try {
				br = new BufferedReader(isr);
				while ((words = readSingleLine(br)) != null) {
					iTotTrainPositive += readWordsIntoMap(words, trainPositive);
					readWordsIntoMap(words, uniqueWords);
				}
				br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private int readWordsIntoMap(String[] words,
			HashMap<String, DocumentBean> map) {
		int count = 0;
		if (map != null && words != null) {
			for (String word : words) {
				if (word != null && !word.equals("") && word.length() != 0
						&& !word.equals("\t")) {
					if (map.containsKey(word)) {
						map.get(word).setCount(map.get(word).getCount() + 1);
					} else {
						DocumentBean bean = new DocumentBean(word, 1);
						map.put(word, bean);
					}
					count++;
				}
			}
		}
		return count;
	}

	private void readIntoNegetiveHashMap(String filePath) {
		InputStream is = null;
		InputStreamReader isr = null;
		BufferedReader br = null;

		try {
			is = new FileInputStream(filePath);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		isr = new InputStreamReader(is);

		String[] words = null;

		File file = new File(filePath);
		if (file.exists()) {
			try {
				br = new BufferedReader(isr);
				while ((words = readSingleLine(br)) != null) {
					iTotTrainNegetive += readWordsIntoMap(words, trainNegetive);
					readWordsIntoMap(words, uniqueWords);
				}
				br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public String[] readSingleLine(BufferedReader brdr) throws IOException {
		String line = null;
		if ((line = brdr.readLine()) != null) {
			 return line.replaceAll("[^a-zA-Z 0-9]",
			 "").toLowerCase().split("\\s+"); //Take only alphabets and numbers and split by space
			 
		}
		return null;
	}

}
