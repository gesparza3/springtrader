import groovy.sql.Sql
import org.apache.tools.ant.taskdefs.SQLExec
import org.apache.tools.ant.Project

def loadProps() {
  def props = new Properties()
  new File("nanotrader.sqlf.properties").withInputStream {
    stream -> props.load(stream)
  }
  p = new ConfigSlurper().parse(props)
}

def checkSchema() {
  Project project = new Project();
  project.init()
  project.setName("Nanotrader DB Check")
  
  //Check if nanotrader table exists
  def url = p.dbURLPrefix + p.dbHost + ":" + p.dbPort
  def sqlf = Sql.newInstance(url, p.dbUser, p.dbPasswd, p.dbDriver)
  def md = sqlf.connection.metaData
  rs = md.getTables(null, 'NANOTRADER', 'ACCOUNT' , null);
  if(rs.next()){
    println 'Schema exists'
    sqlf.close()
    System.exit(0)
    return
  }

  println 'Schema not found'
  sqlf.close()
  System.exit(1)
}

loadProps()
checkSchema()
