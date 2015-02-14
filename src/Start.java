
public class Start {

	public Start() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		long startTime = System.currentTimeMillis();
		
		SATaskSplitter objSATaskSplitter = new SATaskSplitter();
		objSATaskSplitter.categorizeText();
		
		long estimatedTime = System.currentTimeMillis() - startTime;
		System.out.println("It took " + (double) estimatedTime / 60000
				+ " minutes.");
	}

}
