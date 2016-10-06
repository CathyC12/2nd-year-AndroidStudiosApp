package ayry.com.ary_app;

/**
 * Created by stephen on 10/03/2016.
 */
public interface GetUserCallBack {

//informs server request which method to call when finished
       public abstract void finished(ObjectRequestHolder returnedObj);

}
