
public class DocumentBean {

	public String word;
	public int count;
	public String classification;
	public String calculatedClassification;
	public double probability;
	
	public DocumentBean() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param word
	 * @param count
	 */
	public DocumentBean(String word, int count) {
		super();
		this.word = word;
		this.count = count;
	}


	/**
	 * @return the calculatedClassification
	 */
	public String getCalculatedClassification() {
		return calculatedClassification;
	}

	/**
	 * @param calculatedClassification the calculatedClassification to set
	 */
	public void setCalculatedClassification(String calculatedClassification) {
		this.calculatedClassification = calculatedClassification;
	}

	/**
	 * @return the word
	 */
	public String getWord() {
		return word;
	}

	/**
	 * @param word the word to set
	 */
	public void setWord(String word) {
		this.word = word;
	}

	/**
	 * @return the count
	 */
	public int getCount() {
		return count;
	}

	/**
	 * @param count the count to set
	 */
	public void setCount(int count) {
		this.count = count;
	}

	/**
	 * @return the classification
	 */
	public String getClassification() {
		return classification;
	}

	/**
	 * @param classification the classification to set
	 */
	public void setClassification(String classification) {
		this.classification = classification;
	}

	/**
	 * @return the probability
	 */
	public double getProbability() {
		return probability;
	}

	/**
	 * @param probability the probability to set
	 */
	public void setProbability(double probability) {
		this.probability = probability;
	}
	
	

}
