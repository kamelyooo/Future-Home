
package com.examples.marmoush.data;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.persistence.*;
import com.backendless.geo.GeoPoint;

import java.util.List;
import java.util.Date;

public class News
{
  private String imageUrl;
  private String objectId;
  private Date created;
  private String ownerId;
  private Date updated;
  private Integer code;
  public String getImageUrl()
  {
    return imageUrl;
  }

  public void setImageUrl( String imageUrl )
  {
    this.imageUrl = imageUrl;
  }

  public String getObjectId()
  {
    return objectId;
  }

  public Date getCreated()
  {
    return created;
  }

  public String getOwnerId()
  {
    return ownerId;
  }

  public Date getUpdated()
  {
    return updated;
  }

  public Integer getCode()
  {
    return code;
  }

  public void setCode( Integer code )
  {
    this.code = code;
  }

                                                    
  public News save()
  {
    return Backendless.Data.of( News.class ).save( this );
  }

  public void saveAsync( AsyncCallback<News> callback )
  {
    Backendless.Data.of( News.class ).save( this, callback );
  }

  public Long remove()
  {
    return Backendless.Data.of( News.class ).remove( this );
  }

  public void removeAsync( AsyncCallback<Long> callback )
  {
    Backendless.Data.of( News.class ).remove( this, callback );
  }

  public static News findById( String id )
  {
    return Backendless.Data.of( News.class ).findById( id );
  }

  public static void findByIdAsync( String id, AsyncCallback<News> callback )
  {
    Backendless.Data.of( News.class ).findById( id, callback );
  }

  public static News findFirst()
  {
    return Backendless.Data.of( News.class ).findFirst();
  }

  public static void findFirstAsync( AsyncCallback<News> callback )
  {
    Backendless.Data.of( News.class ).findFirst( callback );
  }

  public static News findLast()
  {
    return Backendless.Data.of( News.class ).findLast();
  }

  public static void findLastAsync( AsyncCallback<News> callback )
  {
    Backendless.Data.of( News.class ).findLast( callback );
  }

  public static List<News> find( DataQueryBuilder queryBuilder )
  {
    return Backendless.Data.of( News.class ).find( queryBuilder );
  }

  public static void findAsync( DataQueryBuilder queryBuilder, AsyncCallback<List<News>> callback )
  {
    Backendless.Data.of( News.class ).find( queryBuilder, callback );
  }
}