import java.io.File;
import java.util.ArrayList;

public class SATaskSplitter {

	ArrayList<String> listPositive, listNegetaive;
	ArrayList<SingleFold> listFolds;

	public SATaskSplitter() {
		// TODO Auto-generated constructor stub
		listFolds = new ArrayList<SingleFold>();
	}

	public void categorizeText() {

		File posFolder = new File(new File("").getAbsolutePath() + "/"
				+ "trainingdata/pos");
		File[] positiveFiles = posFolder.listFiles();

		File negFolder = new File(new File("").getAbsolutePath() + "/"
				+ "trainingdata/neg");
		File[] negetiveFiles = negFolder.listFiles();

		ArrayList<String> fnameTrainPositive = null;// new ArrayList<String>();
		ArrayList<String> fnameTrainNegetive = null;// new ArrayList<String>();

		ArrayList<String> fnameTestPositive = null;// new ArrayList<String>();
		ArrayList<String> fnameTestNegetive = null;// new ArrayList<String>();

		int k = 1; // How many folds

		SingleFold objSingleFold = null;
//		System.out.println("Dividing into " + k + " folds...");
		try {
			for (int iIterator = 0; iIterator < k; iIterator++) {
				// System.out.println("Hello "+iIterator);
				switch (iIterator) {
				case 0: {
					fnameTrainPositive = new ArrayList<String>();
					fnameTrainNegetive = new ArrayList<String>();

					fnameTestPositive = new ArrayList<String>();
					fnameTestNegetive = new ArrayList<String>();
					try{
					for (int i = 0; i < positiveFiles.length; i++) {
						if (positiveFiles[i].isFile()
								&& !positiveFiles[i].getName()
										.equalsIgnoreCase(".DS_Store")) {

							fnameTrainPositive.add(positiveFiles[i]
									.getAbsolutePath().toString());
						}
					}
					for (int i = 0; i < negetiveFiles.length; i++) {
						if (negetiveFiles[i].isFile()
								&& !negetiveFiles[i].getName()
										.equalsIgnoreCase(".DS_Store")) {
							fnameTrainNegetive.add(negetiveFiles[i]
									.getAbsolutePath().toString());
						}
					}
					}catch(NullPointerException ne)
					{
						System.out.println("Please add the input data in the same directory as the project jar!");
						System.exit(0);
					}
					objSingleFold = new SingleFold(Integer.toString(iIterator),
							fnameTrainPositive, fnameTrainNegetive,
							fnameTestPositive, fnameTestNegetive);
					break;
				}

				}
				listFolds.add(objSingleFold);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
