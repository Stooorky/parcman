package database;

public interface DBFile
{
	public void save();
	public void load();
	public String getDbFile();
	public void setDbFile(String dbFile);
}
