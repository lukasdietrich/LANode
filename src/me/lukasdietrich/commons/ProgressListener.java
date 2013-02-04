package me.lukasdietrich.commons;

public interface ProgressListener {

	/**
	 * return false if progress should be cancled, true if otherwise
	 *
	 * @param percent
	 * @return
	 */
	public boolean percentChanged(int percent, String... args);
	public void progressDone(boolean success, Object source);
	
}
