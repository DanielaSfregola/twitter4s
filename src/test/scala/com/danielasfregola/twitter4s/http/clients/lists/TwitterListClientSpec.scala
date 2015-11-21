package com.danielasfregola.twitter4s.http.clients.lists

import com.danielasfregola.twitter4s.util.{ClientSpec, ClientSpecContext}
import spray.http.HttpMethods
import spray.http.Uri.Query
import com.danielasfregola.twitter4s.entities._
import com.danielasfregola.twitter4s.entities.enums.Mode

class TwitterListClientSpec extends ClientSpec {

  trait TwitterListClientSpecContext extends ClientSpecContext with TwitterListClient

  "Twitter List Client" should {

    "get lists for a user" in new TwitterListClientSpecContext {
      val result: Seq[TwitterList] = when(getListsForUser("DanielaSfregola")).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/lists/list.json"
        request.uri.query === Query("reverse=false&screen_name=DanielaSfregola")
      }.respondWith("/twitter/lists/lists.json").await
      result === loadJsonAs[Seq[TwitterList]]("/fixtures/lists/lists.json")
    }

    "get lists for a user by id" in new TwitterListClientSpecContext {
      val result: Seq[TwitterList] = when(getListsForUserId(2911461333L)).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/lists/list.json"
        request.uri.query === Query("reverse=false&user_id=2911461333")
      }.respondWith("/twitter/lists/lists.json").await
      result === loadJsonAs[Seq[TwitterList]]("/fixtures/lists/lists.json")
    }

    "get a list timeline by id" in new TwitterListClientSpecContext {
      val result: Seq[Status] = when(getListTimelineByListId(8044403)).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/lists/statuses.json"
        request.uri.query === Query("count=20&include_entities=true&include_rts=false&list_id=8044403")
      }.respondWith("/twitter/lists/timeline.json").await
      result === loadJsonAs[Seq[Status]]("/fixtures/lists/timeline.json")
    }

    "get a list timeline by slug and owner" in new TwitterListClientSpecContext {
      val result: Seq[Status] = when(getListTimelineBySlugAndOwnerName("meetup-20100301", "twitterapi")).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/lists/statuses.json"
        request.uri.query === Query("count=20&include_entities=true&include_rts=false&owner_screen_name=twitterapi&slug=meetup-20100301")
      }.respondWith("/twitter/lists/timeline.json").await
      result === loadJsonAs[Seq[Status]]("/fixtures/lists/timeline.json")
    }

    "get a list timeline by slug and owner id" in new TwitterListClientSpecContext {
      val result: Seq[Status] = when(getListTimelineBySlugAndOwnerId("meetup-20100301", 6253282)).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/lists/statuses.json"
        request.uri.query === Query("count=20&include_entities=true&include_rts=false&owner_id=6253282&slug=meetup-20100301")
      }.respondWith("/twitter/lists/timeline.json").await
      result === loadJsonAs[Seq[Status]]("/fixtures/lists/timeline.json")
    }

    "remove list member by list id and user" in new TwitterListClientSpecContext {
      val result: Unit = when(removeListMemberByListId(8044403, "DanielaSfregola")).expectRequest { request =>
        request.method === HttpMethods.POST
        request.uri.endpoint === "https://api.twitter.com/1.1/lists/members/destroy.json"
        request.uri.query === Query("list_id=8044403&screen_name=DanielaSfregola")
      }.respondWithOk.await
      result === (())
    }

    "remove list member by slug and owner and user" in new TwitterListClientSpecContext {
      val result: Unit = when(removeListMemberBySlugAndOwnerName("meetup-20100301", "twitterapi", "DanielaSfregola")).expectRequest { request =>
        request.method === HttpMethods.POST
        request.uri.endpoint === "https://api.twitter.com/1.1/lists/members/destroy.json"
        request.uri.query === Query("owner_screen_name=twitterapi&screen_name=DanielaSfregola&slug=meetup-20100301")
      }.respondWithOk.await
      result === (())
    }

    "remove list member by slug and owner id and user" in new TwitterListClientSpecContext {
      val result: Unit = when(removeListMemberBySlugAndOwnerId("meetup-20100301", 6253282, "DanielaSfregola")).expectRequest { request =>
        request.method === HttpMethods.POST
        request.uri.endpoint === "https://api.twitter.com/1.1/lists/members/destroy.json"
        request.uri.query === Query("owner_id=6253282&screen_name=DanielaSfregola&slug=meetup-20100301")
      }.respondWithOk.await
      result === (())
    }

    "remove list member by list id and user id" in new TwitterListClientSpecContext {
      val result: Unit = when(removeListMemberIdByListId(8044403, 2911461333L)).expectRequest { request =>
        request.method === HttpMethods.POST
        request.uri.endpoint === "https://api.twitter.com/1.1/lists/members/destroy.json"
        request.uri.query === Query("list_id=8044403&user_id=2911461333")
      }.respondWithOk.await
      result === (())
    }

    "remove list member by slug and owner and user id" in new TwitterListClientSpecContext {
      val result: Unit = when(removeListMemberIdBySlugAndOwnerName("meetup-20100301", "twitterapi", 2911461333L)).expectRequest { request =>
        request.method === HttpMethods.POST
        request.uri.endpoint === "https://api.twitter.com/1.1/lists/members/destroy.json"
        request.uri.query === Query("owner_screen_name=twitterapi&slug=meetup-20100301&user_id=2911461333")
      }.respondWithOk.await
      result === (())
    }

    "remove list member by slug and owner id and user id" in new TwitterListClientSpecContext {
      val result: Unit = when(removeListMemberIdBySlugAndOwnerId("meetup-20100301", 6253282, 2911461333L)).expectRequest { request =>
        request.method === HttpMethods.POST
        request.uri.endpoint === "https://api.twitter.com/1.1/lists/members/destroy.json"
        request.uri.query === Query("owner_id=6253282&slug=meetup-20100301&user_id=2911461333")
      }.respondWithOk.await
      result === (())
    }

    "get list memberships per user" in new TwitterListClientSpecContext {
      val result: TwitterLists = when(getListMembershipsForUser("DanielaSfregola")).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/lists/memberships.json"
        request.uri.query === Query("count=20&cursor=-1&filter_to_owned_lists=false&screen_name=DanielaSfregola")
      }.respondWith("/twitter/lists/memberships.json").await
      result === loadJsonAs[TwitterLists]("/fixtures/lists/memberships.json")
    }

    "get list memberships per user id" in new TwitterListClientSpecContext {
      val result: TwitterLists = when(getListMembershipsForUserId(2911461333L)).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/lists/memberships.json"
        request.uri.query === Query("count=20&cursor=-1&filter_to_owned_lists=false&user_id=2911461333")
      }.respondWith("/twitter/lists/memberships.json").await
      result === loadJsonAs[TwitterLists]("/fixtures/lists/memberships.json")
    }

    "add members ids per list id" in new TwitterListClientSpecContext {
      val result: Unit = when(addListMemberIdsByListId(8044403, Seq(2911461333L, 2911461334L))).expectRequest { request =>
        request.method === HttpMethods.POST
        request.uri.endpoint === "https://api.twitter.com/1.1/lists/members/create_all.json"
        request.uri.query === Query("list_id=8044403&user_id=2911461333,2911461334")
      }.respondWithOk.await
      result === (())
    }
    
    "add members ids per slug and owner" in new TwitterListClientSpecContext {
      val result: Unit = when(addListMemberIdsBySlugAndOwnerName("meetup-20100301", "twitterapi", Seq(2911461333L, 2911461334L))).expectRequest { request =>
        request.method === HttpMethods.POST
        request.uri.endpoint === "https://api.twitter.com/1.1/lists/members/create_all.json"
        request.uri.query === Query("owner_screen_name=twitterapi&slug=meetup-20100301&user_id=2911461333,2911461334")
      }.respondWithOk.await
      result === (())
    }
    
    "add members ids per slug and owner id" in new TwitterListClientSpecContext {
      val result: Unit = when(addListMemberIdsBySlugAndOwnerId("meetup-20100301", 6253282, Seq(2911461333L, 2911461334L))).expectRequest { request =>
        request.method === HttpMethods.POST
        request.uri.endpoint === "https://api.twitter.com/1.1/lists/members/create_all.json"
        request.uri.query === Query("owner_id=6253282&slug=meetup-20100301&user_id=2911461333,2911461334")
      }.respondWithOk.await
      result === (())
    }
    
    "add members per list id" in new TwitterListClientSpecContext {
      val result: Unit = when(addListMembersByListId(8044403, Seq("marcobonzanini", "odersky"))).expectRequest { request =>
        request.method === HttpMethods.POST
        request.uri.endpoint === "https://api.twitter.com/1.1/lists/members/create_all.json"
        request.uri.query === Query("list_id=8044403&screen_name=marcobonzanini,odersky")
      }.respondWithOk.await
      result === (())
    }
    
    "add members per slug and owner" in new TwitterListClientSpecContext {
      val result: Unit = when(addListMembersBySlugAndOwnerName("meetup-20100301", "twitterapi", Seq("marcobonzanini", "odersky"))).expectRequest { request =>
        request.method === HttpMethods.POST
        request.uri.endpoint === "https://api.twitter.com/1.1/lists/members/create_all.json"
        request.uri.query === Query("owner_screen_name=twitterapi&screen_name=marcobonzanini,odersky&slug=meetup-20100301")
      }.respondWithOk.await
      result === (())
    }
    
    "add members per slug and owner id" in new TwitterListClientSpecContext {
      val result: Unit = when(addListMembersBySlugAndOwnerId("meetup-20100301", 6253282, Seq("marcobonzanini", "odersky"))).expectRequest { request =>
        request.method === HttpMethods.POST
        request.uri.endpoint === "https://api.twitter.com/1.1/lists/members/create_all.json"
        request.uri.query === Query("owner_id=6253282&screen_name=marcobonzanini,odersky&slug=meetup-20100301")
      }.respondWithOk.await
      result === (())
    }
    
    "reject 'addListMembersIds' if no ids are provided" in new TwitterListClientSpecContext {
      addListMemberIdsByListId(8044403, Seq.empty) must throwA[IllegalArgumentException]("requirement failed: please, provide at least one user id")
    }
    
    "reject 'addListMembersIdsBySlugAndOwnerName' if no ids are provided" in new TwitterListClientSpecContext {
      addListMemberIdsBySlugAndOwnerName("meetup-20100301", "twitterapi", Seq.empty) must throwA[IllegalArgumentException]("requirement failed: please, provide at least one user id")
    }
    
    "reject 'addListMembersIdsBySlugAndOwnerId' if no ids are provided" in new TwitterListClientSpecContext {
      addListMemberIdsBySlugAndOwnerId("meetup-20100301", 6253282, Seq.empty) must throwA[IllegalArgumentException]("requirement failed: please, provide at least one user id")
    }
    
    "reject 'addListMembers' if no screen names are provided" in new TwitterListClientSpecContext {
      addListMembersByListId(8044403, Seq.empty) must throwA[IllegalArgumentException]("requirement failed: please, provide at least one screen name")
    }
    
    "reject 'addListMembersBySlugAndOwnerName' if no screen names are provided" in new TwitterListClientSpecContext {
      addListMembersBySlugAndOwnerName("meetup-20100301", "twitterapi", Seq.empty) must throwA[IllegalArgumentException]("requirement failed: please, provide at least one screen name")
    }
    
    "reject 'addListMembersBySlugAndOwnerId' if no screen names are provided" in new TwitterListClientSpecContext {
      addListMembersBySlugAndOwnerId("meetup-20100301", 6253282, Seq.empty) must throwA[IllegalArgumentException]("requirement failed: please, provide at least one screen name")
    }

    "get member by id per list id" in new TwitterListClientSpecContext {
      val result: User = when(checkListMemberByUserIdAndListId(8044403, 2911461333L)).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/lists/members/show.json"
        request.uri.query === Query("include_entities=true&list_id=8044403&skip_status=false&user_id=2911461333")
      }.respondWith("/twitter/lists/member.json").await
      result === loadJsonAs[User]("/fixtures/lists/member.json")
    }

    "get member by id per slug and owner" in new TwitterListClientSpecContext {
      val result: User = when(checkListMemberByUserIdSlugAndOwnerName("meetup-20100301", "twitterapi", 2911461333L)).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/lists/members/show.json"
        request.uri.query === Query("include_entities=true&owner_screen_name=twitterapi&skip_status=false&slug=meetup-20100301&user_id=2911461333")
      }.respondWith("/twitter/lists/member.json").await
      result === loadJsonAs[User]("/fixtures/lists/member.json")
    }

    "get member by id per slug and owner id" in new TwitterListClientSpecContext {
      val result: User = when(checkListMemberByUserIdSlugAndOwnerId("meetup-20100301", 6253282, 2911461333L)).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/lists/members/show.json"
        request.uri.query === Query("include_entities=true&owner_id=6253282&skip_status=false&slug=meetup-20100301&user_id=2911461333")
      }.respondWith("/twitter/lists/member.json").await
      result === loadJsonAs[User]("/fixtures/lists/member.json")
    }

    "get member per list id" in new TwitterListClientSpecContext {
      val result: User = when(checkListMemberByUserAndListId(8044403, "DanielaSfregola")).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/lists/members/show.json"
        request.uri.query === Query("include_entities=true&list_id=8044403&screen_name=DanielaSfregola&skip_status=false")
      }.respondWith("/twitter/lists/member.json").await
      result === loadJsonAs[User]("/fixtures/lists/member.json")
    }

    "get member per slug and owner" in new TwitterListClientSpecContext {
      val result: User = when(checkListMemberByUserSlugAndOwnerName("meetup-20100301", "twitterapi", "DanielaSfregola")).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/lists/members/show.json"
        request.uri.query === Query("include_entities=true&owner_screen_name=twitterapi&screen_name=DanielaSfregola&skip_status=false&slug=meetup-20100301")
      }.respondWith("/twitter/lists/member.json").await
      result === loadJsonAs[User]("/fixtures/lists/member.json")
    }

    "get member per slug and owner id" in new TwitterListClientSpecContext {
      val result: User = when(checkListMemberByUserSlugAndOwnerId("meetup-20100301", 6253282, "DanielaSfregola")).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/lists/members/show.json"
        request.uri.query === Query("include_entities=true&owner_id=6253282&screen_name=DanielaSfregola&skip_status=false&slug=meetup-20100301")
      }.respondWith("/twitter/lists/member.json").await
      result === loadJsonAs[User]("/fixtures/lists/member.json")
    }

    "get members of list per list id" in new TwitterListClientSpecContext {
      val result: Users = when(getListMembersByListId(8044403)).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/lists/members.json"
        request.uri.query === Query("count=20&cursor=-1&include_entities=true&list_id=8044403&skip_status=false")
      }.respondWith("/twitter/lists/members.json").await
      result === loadJsonAs[Users]("/fixtures/lists/members.json")
    }

    "get members of list per slug and owner" in new TwitterListClientSpecContext {
      val result: Users = when(getListMembersBySlugAndOwnerName("meetup-20100301", "twitterapi")).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/lists/members.json"
        request.uri.query === Query("count=20&cursor=-1&include_entities=true&owner_screen_name=twitterapi&skip_status=false&slug=meetup-20100301")
      }.respondWith("/twitter/lists/members.json").await
      result === loadJsonAs[Users]("/fixtures/lists/members.json")
    }

    "get members of list per slug and owner id" in new TwitterListClientSpecContext {
      val result: Users = when(getListMembersBySlugAndOwnerId("meetup-20100301", 6253282)).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/lists/members.json"
        request.uri.query === Query("count=20&cursor=-1&include_entities=true&owner_id=6253282&skip_status=false&slug=meetup-20100301")
      }.respondWith("/twitter/lists/members.json").await
      result === loadJsonAs[Users]("/fixtures/lists/members.json")
    }

    "add member id to list id" in new TwitterListClientSpecContext {
      val result: Unit = when(addListMemberIdByListId(8044403, 2911461333L)).expectRequest { request =>
        request.method === HttpMethods.POST
        request.uri.endpoint === "https://api.twitter.com/1.1/lists/members/create.json"
        request.uri.query === Query("list_id=8044403&user_id=2911461333")
      }.respondWithOk.await
      result === (())
    }

    "add member id to slug and owner" in new TwitterListClientSpecContext {
      val result: Unit = when(addListMemberIdBySlugAndOwnerName("meetup-20100301", "twitterapi", 2911461333L)).expectRequest { request =>
        request.method === HttpMethods.POST
        request.uri.endpoint === "https://api.twitter.com/1.1/lists/members/create.json"
        request.uri.query === Query("owner_screen_name=twitterapi&slug=meetup-20100301&user_id=2911461333")
      }.respondWithOk.await
      result === (())
    }

    "add member id to slug and owner id" in new TwitterListClientSpecContext {
      val result: Unit = when(addListMemberIdBySlugAndOwnerId("meetup-20100301", 6253282, 2911461333L)).expectRequest { request =>
        request.method === HttpMethods.POST
        request.uri.endpoint === "https://api.twitter.com/1.1/lists/members/create.json"
        request.uri.query === Query("owner_id=6253282&slug=meetup-20100301&user_id=2911461333")
      }.respondWithOk.await
      result === (())
    }

    "add member per list id" in new TwitterListClientSpecContext {
      val result: Unit = when(addListMemberByListId(8044403, "marcobonzanini")).expectRequest { request =>
        request.method === HttpMethods.POST
        request.uri.endpoint === "https://api.twitter.com/1.1/lists/members/create.json"
        request.uri.query === Query("list_id=8044403&screen_name=marcobonzanini")
      }.respondWithOk.await
      result === (())
    }

    "add member per slug and owner" in new TwitterListClientSpecContext {
      val result: Unit = when(addListMemberBySlugAndOwnerName("meetup-20100301", "twitterapi", "marcobonzanini")).expectRequest { request =>
        request.method === HttpMethods.POST
        request.uri.endpoint === "https://api.twitter.com/1.1/lists/members/create.json"
        request.uri.query === Query("owner_screen_name=twitterapi&screen_name=marcobonzanini&slug=meetup-20100301")
      }.respondWithOk.await
      result === (())
    }

    "add member per slug and owner id" in new TwitterListClientSpecContext {
      val result: Unit = when(addListMemberBySlugAndOwnerId("meetup-20100301", 6253282, "marcobonzanini")).expectRequest { request =>
        request.method === HttpMethods.POST
        request.uri.endpoint === "https://api.twitter.com/1.1/lists/members/create.json"
        request.uri.query === Query("owner_id=6253282&screen_name=marcobonzanini&slug=meetup-20100301")
      }.respondWithOk.await
      result === (())
    }

    "delete a list by id" in new TwitterListClientSpecContext {
      val result: TwitterList = when(deleteListById(8044403)).expectRequest { request =>
        request.method === HttpMethods.POST
        request.uri.endpoint === "https://api.twitter.com/1.1/lists/destroy.json"
        request.uri.query === Query("list_id=8044403")
      }.respondWith("/twitter/lists/destroy.json").await
      result === loadJsonAs[TwitterList]("/fixtures/lists/destroy.json")
    }

    "delete a list by slug and owner id" in new TwitterListClientSpecContext {
      val result: TwitterList = when(deleteListBySlugAndOwnerId("meetup-20100301", 6253282)).expectRequest { request =>
        request.method === HttpMethods.POST
        request.uri.endpoint === "https://api.twitter.com/1.1/lists/destroy.json"
        request.uri.query === Query("owner_id=6253282&slug=meetup-20100301")
      }.respondWith("/twitter/lists/destroy.json").await
      result === loadJsonAs[TwitterList]("/fixtures/lists/destroy.json")
    }

    "delete a list by slug and owner" in new TwitterListClientSpecContext {
      val result: TwitterList = when(deleteListBySlugAndOwnerName("meetup-20100301", "twitterapi")).expectRequest { request =>
        request.method === HttpMethods.POST
        request.uri.endpoint === "https://api.twitter.com/1.1/lists/destroy.json"
        request.uri.query === Query("owner_screen_name=twitterapi&slug=meetup-20100301")
      }.respondWith("/twitter/lists/destroy.json").await
      result === loadJsonAs[TwitterList]("/fixtures/lists/destroy.json")
    }

    "update list mode by id" in new TwitterListClientSpecContext {
      val result: Unit = when(updateListMode(8044403, Mode.Private)).expectRequest { request =>
        request.method === HttpMethods.POST
        request.uri.endpoint === "https://api.twitter.com/1.1/lists/update.json"
        request.uri.query === Query("list_id=8044403&mode=private")
      }.respondWithOk.await
      result === (())
    }

    "update list mode by slug and owner name" in new TwitterListClientSpecContext {
      val result: Unit = when(updateListModeBySlugAndOwnerName("meetup-20100301", "twitterapi", Mode.Private)).expectRequest { request =>
        request.method === HttpMethods.POST
        request.uri.endpoint === "https://api.twitter.com/1.1/lists/update.json"
        request.uri.query === Query("mode=private&owner_screen_name=twitterapi&slug=meetup-20100301")
      }.respondWithOk.await
      result === (())
    }

    "update list mode by slug and owner id" in new TwitterListClientSpecContext {
      val result: Unit = when(updateListModeBySlugAndOwnerId("meetup-20100301", 6253282, Mode.Private)).expectRequest { request =>
        request.method === HttpMethods.POST
        request.uri.endpoint === "https://api.twitter.com/1.1/lists/update.json"
        request.uri.query === Query("mode=private&owner_id=6253282&slug=meetup-20100301")
      }.respondWithOk.await
      result === (())
    }

    "update list name by id" in new TwitterListClientSpecContext {
      val result: Unit = when(updateListName(8044403, "new name")).expectRequest { request =>
        request.method === HttpMethods.POST
        request.uri.endpoint === "https://api.twitter.com/1.1/lists/update.json"
        request.uri.query === Query("list_id=8044403&name=new+name")
      }.respondWithOk.await
      result === (())
    }

    "update list name by slug and owner name" in new TwitterListClientSpecContext {
      val result: Unit = when(updateListNameBySlugAndOwnerName("meetup-20100301", "twitterapi", "new name")).expectRequest { request =>
        request.method === HttpMethods.POST
        request.uri.endpoint === "https://api.twitter.com/1.1/lists/update.json"
        request.uri.query === Query("name=new+name&owner_screen_name=twitterapi&slug=meetup-20100301")
      }.respondWithOk.await
      result === (())
    }

    "update list name by slug and owner id" in new TwitterListClientSpecContext {
      val result: Unit = when(updateListNameBySlugAndOwnerId("meetup-20100301", 6253282, "new name")).expectRequest { request =>
        request.method === HttpMethods.POST
        request.uri.endpoint === "https://api.twitter.com/1.1/lists/update.json"
        request.uri.query === Query("name=new+name&owner_id=6253282&slug=meetup-20100301")
      }.respondWithOk.await
      result === (())
    }

    "update list description by id" in new TwitterListClientSpecContext {
      val result: Unit = when(updateListDescription(8044403, "cool description")).expectRequest { request =>
        request.method === HttpMethods.POST
        request.uri.endpoint === "https://api.twitter.com/1.1/lists/update.json"
        request.uri.query === Query("description=cool+description&list_id=8044403")
      }.respondWithOk.await
      result === (())
    }

    "update list description by slug and owner name" in new TwitterListClientSpecContext {
      val result: Unit = when(updateListDescriptionBySlugAndOwnerName("meetup-20100301", "twitterapi", "cool description")).expectRequest { request =>
        request.method === HttpMethods.POST
        request.uri.endpoint === "https://api.twitter.com/1.1/lists/update.json"
        request.uri.query === Query("description=cool+description&owner_screen_name=twitterapi&slug=meetup-20100301")
      }.respondWithOk.await
      result === (())
    }

    "update list description by slug and owner id" in new TwitterListClientSpecContext {
      val result: Unit = when(updateListDescriptionBySlugAndOwnerId("meetup-20100301", 6253282, "cool description")).expectRequest { request =>
        request.method === HttpMethods.POST
        request.uri.endpoint === "https://api.twitter.com/1.1/lists/update.json"
        request.uri.query === Query("description=cool+description&owner_id=6253282&slug=meetup-20100301")
      }.respondWithOk.await
      result === (())
    }

    "update list by id" in new TwitterListClientSpecContext {
      val update = TwitterListUpdate(mode = Some(Mode.Private), description = Some("cool description"))
      val result: Unit = when(updateList(8044403, update)).expectRequest { request =>
        request.method === HttpMethods.POST
        request.uri.endpoint === "https://api.twitter.com/1.1/lists/update.json"
        request.uri.query === Query("description=cool+description&list_id=8044403&mode=private")
      }.respondWithOk.await
      result === (())
    }

    "update list by slug and owner name" in new TwitterListClientSpecContext {
      val update = TwitterListUpdate(mode = Some(Mode.Private), description = Some("cool description"))
      val result: Unit = when(updateListBySlugAndOwnerName("meetup-20100301", "twitterapi", update)).expectRequest { request =>
        request.method === HttpMethods.POST
        request.uri.endpoint === "https://api.twitter.com/1.1/lists/update.json"
        request.uri.query === Query("description=cool+description&mode=private&owner_screen_name=twitterapi&slug=meetup-20100301")
      }.respondWithOk.await
      result === (())
    }

    "update list by slug and owner id" in new TwitterListClientSpecContext {
      val update = TwitterListUpdate(mode = Some(Mode.Private), description = Some("cool description"))
      val result: Unit = when(updateListBySlugAndOwnerId("meetup-20100301", 6253282, update)).expectRequest { request =>
        request.method === HttpMethods.POST
        request.uri.endpoint === "https://api.twitter.com/1.1/lists/update.json"
        request.uri.query === Query("description=cool+description&mode=private&owner_id=6253282&slug=meetup-20100301")
      }.respondWithOk.await
      result === (())
    }

    "create a list" in new TwitterListClientSpecContext {
      val result: TwitterList = when(createList("my-list", Mode.Private, Some("a nice description"))).expectRequest { request =>
        request.method === HttpMethods.POST
        request.uri.endpoint === "https://api.twitter.com/1.1/lists/create.json"
        request.uri.query === Query("description=a+nice+description&mode=private&name=my-list")
      }.respondWith("/twitter/lists/create.json").await
      result === loadJsonAs[TwitterList]("/fixtures/lists/create.json")
    }

    "get list by id" in new TwitterListClientSpecContext {
      val result: TwitterList = when(getListById(222669735)).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/lists/show.json"
        request.uri.query === Query("list_id=222669735")
      }.respondWith("/twitter/lists/show.json").await
      result === loadJsonAs[TwitterList]("/fixtures/lists/show.json")
    }

    "get list by slug and owner name" in new TwitterListClientSpecContext {
      val result: TwitterList = when(getListBySlugAndOwnerName("my-list", "Daniela Sfregola")).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/lists/show.json"
        request.uri.query === Query("owner_screen_name=Daniela+Sfregola&slug=my-list")
      }.respondWith("/twitter/lists/show.json").await
      result === loadJsonAs[TwitterList]("/fixtures/lists/show.json")
    }

    "get list by slug and owner id" in new TwitterListClientSpecContext {
      val result: TwitterList = when(getListBySlugAndOwnerId("my-list", 2911461333L)).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/lists/show.json"
        request.uri.query === Query("owner_id=2911461333&slug=my-list")
      }.respondWith("/twitter/lists/show.json").await
      result === loadJsonAs[TwitterList]("/fixtures/lists/show.json")
    }

    "get list subscriptions" in new TwitterListClientSpecContext {
      val result: TwitterLists = when(getListSubscriptions("DanielaSfregola")).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/lists/subscriptions.json"
        request.uri.query === Query("count=20&cursor=-1&screen_name=DanielaSfregola")
      }.respondWith("/twitter/lists/subscriptions.json").await
      result === loadJsonAs[TwitterLists]("/fixtures/lists/subscriptions.json")
    }

    "get list subscriptions by user id" in new TwitterListClientSpecContext {
      val result: TwitterLists = when(getListSubscriptionsByUserId(2911461333L)).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/lists/subscriptions.json"
        request.uri.query === Query("count=20&cursor=-1&user_id=2911461333")
      }.respondWith("/twitter/lists/subscriptions.json").await
      result === loadJsonAs[TwitterLists]("/fixtures/lists/subscriptions.json")
    }

    "remove members from list" in new TwitterListClientSpecContext {
      val result: Unit = when(removeListMembers(222669735, Seq("marcobonzanini", "odersky"))).expectRequest { request =>
        request.method === HttpMethods.POST
        request.uri.endpoint === "https://api.twitter.com/1.1/lists/members/destroy_all.json"
        request.uri.query === Query("list_id=222669735&screen_name=marcobonzanini,odersky")
      }.respondWithOk.await
      result === (())
    }

    "remove members from list by slug and owner name" in new TwitterListClientSpecContext {
      val result: Unit = when(removeListMembersBySlugAndOwnerName("my-list", "DanielaSfregola", Seq("marcobonzanini", "odersky"))).expectRequest { request =>
        request.method === HttpMethods.POST
        request.uri.endpoint === "https://api.twitter.com/1.1/lists/members/destroy_all.json"
        request.uri.query === Query("owner_screen_name=DanielaSfregola&screen_name=marcobonzanini,odersky&slug=my-list")
      }.respondWithOk.await
      result === (())
    }

    "remove members from list by slug and owner id" in new TwitterListClientSpecContext {
      val result: Unit = when(removeListMembersBySlugAndOwnerId("my-list", 2911461333L, Seq("marcobonzanini", "odersky"))).expectRequest { request =>
        request.method === HttpMethods.POST
        request.uri.endpoint === "https://api.twitter.com/1.1/lists/members/destroy_all.json"
        request.uri.query === Query("owner_id=2911461333&screen_name=marcobonzanini,odersky&slug=my-list")
      }.respondWithOk.await
      result === (())
    }

    "remove members ids from list" in new TwitterListClientSpecContext {
      val result: Unit = when(removeListMembersIds(222669735, Seq(2911461333L, 2911461334L))).expectRequest { request =>
        request.method === HttpMethods.POST
        request.uri.endpoint === "https://api.twitter.com/1.1/lists/members/destroy_all.json"
        request.uri.query === Query("list_id=222669735&user_id=2911461333,2911461334")
      }.respondWithOk.await
      result === (())
    }

    "remove members ids from list by slug and owner name" in new TwitterListClientSpecContext {
      val result: Unit = when(removeListMembersIdsBySlugAndOwnerName("my-list", "DanielaSfregola", Seq(2911461333L, 2911461334L))).expectRequest { request =>
        request.method === HttpMethods.POST
        request.uri.endpoint === "https://api.twitter.com/1.1/lists/members/destroy_all.json"
        request.uri.query === Query("owner_screen_name=DanielaSfregola&slug=my-list&user_id=2911461333,2911461334")
      }.respondWithOk.await
      result === (())
    }

    "remove members ids from list by slug and owner id" in new TwitterListClientSpecContext {
      val result: Unit = when(removeListMembersIdsBySlugAndOwnerId("my-list", 2911461333L, Seq(2911461333L, 2911461334L))).expectRequest { request =>
        request.method === HttpMethods.POST
        request.uri.endpoint === "https://api.twitter.com/1.1/lists/members/destroy_all.json"
        request.uri.query === Query("owner_id=2911461333&slug=my-list&user_id=2911461333,2911461334")
      }.respondWithOk.await
      result === (())
    }

    "reject 'removeListMembers' if no screen names are provided" in new TwitterListClientSpecContext {
      removeListMembers(222669735, Seq.empty) must throwA[IllegalArgumentException]("requirement failed: please, provide at least one screen name")
    }

    "reject 'removeListMembersBySlugAndOwnerName' if no screen names are provided" in new TwitterListClientSpecContext {
      removeListMembersBySlugAndOwnerName("my-list", "DanielaSfregola", Seq.empty) must throwA[IllegalArgumentException]("requirement failed: please, provide at least one screen name")
    }

    "reject 'removeListMembersBySlugAndOwnerId' if no screen names are provided" in new TwitterListClientSpecContext {
      removeListMembersBySlugAndOwnerId("my-list", 2911461333L, Seq.empty) must throwA[IllegalArgumentException]("requirement failed: please, provide at least one screen name")
    }

    "reject 'removeListMembersIds' if no ids are provided" in new TwitterListClientSpecContext {
      removeListMembersIds(222669735, Seq.empty) must throwA[IllegalArgumentException]("requirement failed: please, provide at least one user id")
    }

    "reject 'removeListMembersIdsBySlugAndOwnerName' if no ids are provided" in new TwitterListClientSpecContext {
      removeListMembersIdsBySlugAndOwnerName("my-list", "DanielaSfregola", Seq.empty) must throwA[IllegalArgumentException]("requirement failed: please, provide at least one user id")
    }

    "reject 'addListMembersIds' if no ids are provided" in new TwitterListClientSpecContext {
      removeListMembersIdsBySlugAndOwnerId("my-list", 2911461333L, Seq.empty) must throwA[IllegalArgumentException]("requirement failed: please, provide at least one user id")
    }

    "get ownerships for user" in new TwitterListClientSpecContext {
      val result: TwitterLists = when(getListOwnerships("DanielaSfregola")).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/lists/ownerships.json"
        request.uri.query === Query("count=20&cursor=-1&screen_name=DanielaSfregola")
      }.respondWith("/twitter/lists/ownerships.json").await
      result === loadJsonAs[TwitterLists]("/fixtures/lists/ownerships.json")
    }

    "get ownerships for user by id" in new TwitterListClientSpecContext {
      val result: TwitterLists = when(getListOwnershipsForUserId(2911461333L)).expectRequest { request =>
        request.method === HttpMethods.GET
        request.uri.endpoint === "https://api.twitter.com/1.1/lists/ownerships.json"
        request.uri.query === Query("count=20&cursor=-1&user_id=2911461333")
      }.respondWith("/twitter/lists/ownerships.json").await
      result === loadJsonAs[TwitterLists]("/fixtures/lists/ownerships.json")
    }
  }

}
