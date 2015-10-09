package twitter4s.entities.enums

object ContributorType extends Enumeration {
   type ContributorType = Value

   val ALL = Value("all")
   val FOLLOWING = Value("following")
   val NONE = Value("none")
 }
