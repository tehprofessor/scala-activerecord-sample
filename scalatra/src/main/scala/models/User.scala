package models

import com.github.aselab.activerecord._
import com.github.aselab.activerecord.dsl._

case class User(
  @Required var name: String,
  @Required @Unique var login: String,
  @Email var email: String
) extends ActiveRecord {
  lazy val projects = hasAndBelongsToMany[Project]
  lazy val memberships = hasMany[Membership]

  @Length(min=8)
  @Confirm
  @Transient
  var password: String = _

  @Transient
  var passwordConfirmation: String = _

  var hashedPassword: String = _

  override def beforeSave() {
    if (password != null && password != "")
      hashedPassword = User.md5digest(password)
  }
}

object User extends ActiveRecordCompanion[User] {
  val md5 = java.security.MessageDigest.getInstance("MD5")

  def md5digest(str: String): String = {
    md5.digest(str.getBytes).map("%02x".format(_)).mkString
  }

  def authenticate(login: String, password: String): Option[User] = {
    findBy("login", login).filter(u => u.hashedPassword == md5digest(password))
  }
}
