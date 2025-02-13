package fr.codecake.ecom.order.domain.user.aggregate;

import fr.codecake.ecom.order.domain.user.vo.*;

import java.time.Instant;
import java.util.Set;

public class User {

  private UserFirstname userFirstname;
  private UserLastname userLastname;
  private UserAddress userAddress;
  private UserEmail userEmail;
  private UserPublicId userPublicId;
  private Instant createdDate;
  private  Instant lastModifiedDate;
  private Long Dbid;
  private Set<Authority> authorities;
}
