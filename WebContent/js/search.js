$(document).ready(function() {


    var p_message = $("#message");
    var notification_window = $("#notification_window");

    var search_forum_button = $("#big_search");
    var start_search = $("#start_search");

    var page_forums = $("#page_forums");
    var page_topics = $("#page_topics");
    var page_replies = $("#page_replies");
    var page_users = $("#page_users");

    var logged_in = $("#logged_in");
    var logged_out = $("#logged_out");
    var logged_user = $("#logged_user");
    var admin_options = $(".admin_options");
    var btn_logout = $("#btn_logout");

    $(btn_logout).click(function(e) {
      e.stopPropagation();
      if (confirm("Are you sure you want to logout!")) {
        $.get("LogoutServlet", function(data) {
          logged_in.hide();
          logged_out.show();
          window.location.replace("index.html");

        });
      }
    });

    $(btn_pannel).click(function(e) {
      e.stopPropagation();
        window.location.replace("user.html?username="+logged_user.text());

    });

    $(search_forum_button).click(function(e) {
      e.stopPropagation();
        window.location.replace("search.html");

    });


    ///search poco
    $(start_search).click(function(e) {
      e.preventDefault();
      e.stopPropagation();
      page_forums.find('tr:gt(0)').remove();
      page_topics.find('tr:gt(0)').remove();
      page_replies.find('tr:gt(0)').remove();
      page_users.find('tr:gt(0)').remove();

      var search_term = $("#search_term");
      var term = search_term.val();
      var search_forums = $("#cb_forums").is(":checked");
      var search_topics = $("#cb_topics").is(":checked");
      var search_replies = $("#cb_replies").is(":checked");
      var search_users = $("#cb_users").is(":checked");

      if (term==="") {
        p_message.text("Cant search nothing")
        notification_window.show().delay(2000).fadeOut();
      }else{

//          checkbox odlucuje kako ce request da izgleda
      if(search_forums){
        var search_req = {
          'search_forums' : search_forums,
          'term': term
        }

        $.get("SearchServlet", search_req, function(data) {
          page_forums.find('tr:gt(0)').remove();
          for (var i = 0; i < data.length; i++) {
            appendForum(data[i]);
          }

        });

        }


        if(search_topics){
          var search_req = {
            'search_topics' : search_topics,
            'term': term
          }

          $.get("SearchServlet", search_req, function(data) {
            page_topics.find('tr:gt(0)').remove();
            for (var i = 0; i < data.length; i++) {
              appendTopic(data[i]);
            }

          });

          }

          if(search_replies){
            var search_req = {
              'search_replies' : search_replies,
              'term': term
            }

            $.get("SearchServlet", search_req, function(data) {
              page_replies.find('tr:gt(0)').remove();
              for (var i = 0; i < data.length; i++) {
                appendReply(data[i]);
              }

            });

            }

            if(search_users){
              var search_req = {
                'search_users' : search_users,
                'term': term
              }

              $.get("SearchServlet", search_req, function(data) {
                page_users.find('tr:gt(0)').remove();
                for (var i = 0; i < data.length; i++) {
                  appendUser(data[i]);

                }

              });

              }




      }
      




    });

    ///kraj searcha

    $.get("LoginServlet", function(data) {
      if (data.status === "yes") {
        //dodaj handlere za pannel i search kad je ulogovan
        logged_out.hide();
        logged_user.text(data.username);
        logged_in.show();
        if (data.role === "admin") {
          loggedInInterface();
        } else if (data.role === "registered") {
          loggedInInterface();
        } else if (data.role === "moderator") {
          loggedInInterface();
        } ////else if neka druga uloga radi drugi posao

      } else {







        //////NOT LOGGED IN
        // turn on/off register window
        var register_window = $(".register_window");
        var btn_register = $("#btn_register");
        var register_close = $("#register_close");

        function resetRegisterInput() {
          $("#reg_usn").val("");
          $("#reg_psw").val("");
          $("#reg_email").val("");
          $("#reg_fname").val("");
          $("#reg_lname").val("");

        }

        $(btn_register).click(function(e) {
          e.stopPropagation();
          if (register_window.is(':hidden')) {
            resetLoginInput();
            login_window.hide();
            register_window.show();

          }
        });

        $(register_close).click(function(e) {
          e.stopPropagation();
          if (register_window.is(':visible')) {
            resetRegisterInput();
            register_window.hide();
          }
        });

        ///collect information from register_window
        // and send to the servlet
        $("#register_submit").click(function(e) {
          e.preventDefault();
          var reg_usn = $("#reg_usn").val();
          var reg_psw = $("#reg_psw").val();
          var reg_email = $("#reg_email").val();
          var reg_fname = $("#reg_fname").val();
          var reg_lname = $("#reg_lname").val();
          if (reg_usn === "" || reg_psw === "" || reg_email === "") {
            p_message.text("* Marked fields must be filled")
            notification_window.show().delay(2000).fadeOut();
          } else {

            var jsonNewUser = {
              'username': reg_usn,
              'password': reg_psw,
              'email': reg_email,
              'fname': reg_fname,
              'lname': reg_lname
            };

            $.post('UserServlet', jsonNewUser, function(data) {
              p_message.text(data.message);
              notification_window.show().delay(2000).fadeOut();
              if (data.status === "success") {
                resetRegisterInput();
                register_window.hide();
              }
            });
          }
        });


        // turn on/off login window
        var login_window = $(".login_window");
        var btn_login = $("#btn_login");
        var login_close = $("#login_close");

        function resetLoginInput() {
          $("#login_username").val("");
          $("#login_password").val("");
        }

        $(btn_login).click(function(e) {
          e.stopPropagation();
          if (login_window.is(':hidden')) {
            resetRegisterInput();
            register_window.hide();
            login_window.show();

          }
        });

        $(login_close).click(function(e) {
          e.stopPropagation();
          if (login_window.is(':visible')) {
            resetLoginInput();
            login_window.hide();
          }
        });

        ///collect information from login_window
        // and send to the servlet
        $("#login_submit").click(function(e) {
          e.preventDefault();
          var log_usn = $("#login_username").val();
          var log_psw = $("#login_password").val();
          if (log_usn === "" || log_psw === "") {
            p_message.text("* Marked fields must be filled")
            notification_window.show().delay(2000).fadeOut();
          } else {

            var jsonUser = {
              'username': log_usn,
              'password': log_psw
            };

            $.post('LoginServlet', jsonUser, function(data) {
              p_message.text(data.message);
              notification_window.show().delay(2000).fadeOut();
              if (data.status === "success") {
                resetLoginInput();
                login_window.hide();
                logged_out.hide();
                logged_user.text(data.username);
                logged_in.show();
                if (data.role === "admin") {
                  loggedInInterface();
                } else if (data.role === "registered") {
                  loggedInInterface();
                } else if (data.role === "moderator") {
                  loggedInInterface();

                }

              }
            });
          }
        });



      } ///nije ulogovan kraj

          });

      function loggedInInterface() {

        page_forums.find('tr:gt(0)').remove();
        page_topics.find('tr:gt(0)').remove();
        page_replies.find('tr:gt(0)').remove();
        page_users.find('tr:gt(0)').remove();



      }

      function forumStatus(boolean) {
        if (boolean) {
          return '<span class="glyphicon glyphicon-lock" style="display: inline;" title="Locked"></span>';
        } else {
          return '<span class="glyphicon glyphicon-ok" style="display: inline;" title="Unlocked"></span>';
        }

      }


      function appendForum(forum) {
        page_forums.append(
          '<tr>' +
          '<td>' +
          '<a title="' + forum.description + '" href="forum.html?id=' +
          forum.id + '">' +
          forum.name +
          '</a>' +
          '</td>' +
          '<td><a href="user.html?username=' + forum.ownerUsername + '">' +
          forum.ownerUsername +
          '</a></td>' +
          '<td>' +
          forum.creationDate +
          '</td>' +
          '<td class="status">' +
          forumStatus(forum.locked) +
          '</td>' +
          '  </tr>'
        );

      }


      ////sredi ovu funkciju da bude lepa
      function getForums(role) {
        if (role === "admin") {
          var req = {
            "forum_id": forumParams.get('id'),
            "forum_page": forum_page,
            "forum_results": forum_results
          }

          $.get("ForumServlet", req, function(data) {
            page_forums.find('tr:gt(0)').remove();
            for (var i = 0; i < data.forums.length; i++) {
              appendForumAdmin(data.forums[i]);
            }
            appendForumDetails(data.details);

          });

        }
        // else if (role === "moderator") {
        //   var req = {
        //     "forum_id": forumParams.get('id'),
        //     "forum_page": forum_page,
        //     "forum_results": forum_results
        //   }
        //
        //   $.get("ForumServlet", req, function(data) {
        //     page_forums.find('tr:gt(0)').remove();
        //     for (var i = 0; i < data.forums.length; i++) {
        //       appendForumGuest(data.forums[i]);
        //     }
        //     appendForumDetails(data.details);
        //
        //   });
        //
        // } else if (role === "registered") {
        //   var req = {
        //     "forum_id": forumParams.get('id'),
        //     "forum_page": forum_page,
        //     "forum_results": forum_results
        //   }
        //
        //   $.get("ForumServlet", req, function(data) {
        //     page_forums.find('tr:gt(0)').remove();
        //     if (data.status === "failure") {
        //       p_message.text(data.message)
        //       notification_window.show().delay(2000).fadeOut();
        //     } else {
        //       for (var i = 0; i < data.forums.length; i++) {
        //         appendForumGuest(data.forums[i]);
        //       }
        //     }
        //     appendForumDetails(data.details);
        //
        //   });
        //
        // } else {
        //   var req = {
        //     "forum_id": forumParams.get('id'),
        //     "forum_page": forum_page,
        //     "forum_results": forum_results
        //   }
        //
        //   $.get("ForumServlet", req, function(data) {
        //     page_forums.find('tr:gt(0)').remove();
        //     // console.log(data.details);
        //     if (data.status === "failure") {
        //       p_message.text(data.message)
        //       notification_window.show().delay(2000).fadeOut();
        //     } else {
        //       for (var i = 0; i < data.forums.length; i++) {
        //         appendForumGuest(data.forums[i]);
        //       }
        //     }
        //     appendForumDetails(data.details);
        //
        //   });
        // }

      }

      function topicStatus(locked, pinned) {
        if (locked && pinned) {
          return '<span class="glyphicon glyphicon-lock" style="display: inline;" title="Locked"> </span><span class="glyphicon glyphicon-flag" style="display: inline" title="Pinned"</span>';
        } else if (locked && !pinned) {
          return '<span class="glyphicon glyphicon-lock" style="display: inline;" title="Locked"> </span>';
        } else if (!locked && pinned) {
          return '<span class="glyphicon glyphicon-flag" style="display: inline" title="Pinned"</span>';
        } else {
          return '<span class="glyphicon glyphicon-ok" style="display: inline;" title="Unlocked"></span>';
        }


      }


      function appendTopic(topic) {
        page_topics.append(
          '<tr>' +
          '<td>' +
          '<a title="' + topic.description + '" href="topic.html?id=' +
          topic.id + '">' +
          topic.name +
          '</a>' +
          '</td>' +
          '<td><a href="user.html?username=' + topic.ownerUsername + '">' +
          topic.ownerUsername +
          '</a></td>' +
          '<td>' +
          topic.creationDate +
          '</td>' +
          '<td class="status">' +
          topicStatus(topic.locked, topic.pinned) +
          '</td>' +
          '  </tr>'
        );

      }


      function appendReply(reply) {
        page_replies.append(
          '<tr><td>' +
          '<a href="reply.html?id=' + reply.id + '">Reply</a>' +
          '<p>' + reply.creationDate + '</p>' +
          '<a href="user.html?username=' + reply.ownerUsername + '">' +
          '<img src="'+reply.ownerPicture+'" alt="img">' + reply.ownerUsername + '</a>' +
          '</td>' +
          '<td><textarea id="reply_content'+reply.id+'" name="" class="" cols="80" rows="5" readonly="true"></textarea></td></tr>'
        )
        page_replies.find("#reply_content"+reply.id).text(reply.content);

      }


      function userStatus(bool) {
        if (!bool) {
          return '<span class="glyphicon glyphicon-ok" style="display: inline;" title="Not Banned"></span>';
        } else {
          return '<span class="glyphicon glyphicon-ban-circle" style="display: inline;" title="Banned"></span>';
        }
      }

      function appendUser(user) {
        page_users.append(
          '<tr>' +
          '<td> <img src="' + user.picture + '" alt="pi" style="height:60px; width:60px;"> </td>' +
          '  <td class="usn"> <a href=user.html?username='+user.username+ '>' + user.username +'</a>' + ' </td>' +
          '<td class="mail"> ' + user.mail + ' </td>' +
          '  <td> ' + user.dateReg + ' </td>' +
          '  <td class="role"> ' + user.role + ' </td>' +
          '<td class="status"> ' + userStatus(user.banned) +
          ' </td>' +
          '</tr>'
        );
      }






      // function getTopics(role) {
      //   if (role === "admin") {
      //     var req = {
      //       "forum_id": forumParams.get('id'),
      //       "topic_page": topic_page,
      //       "topic_results": topic_results
      //     }
      //
      //     $.get("TopicServlet", req, function(data) {
      //       page_topics.find('tr:gt(0)').remove();
      //       for (var i = 0; i < data.length; i++) {
      //         appendTopicAdmin(data[i]);
      //       }
      //
      //     });
      //
      //   } else if (role === "moderator") {
      //     var req = {
      //       "forum_id": forumParams.get('id'),
      //       "topic_page": topic_page,
      //       "topic_results": topic_results
      //     }
      //
      //     $.get("TopicServlet", req, function(data) {
      //       page_topics.find('tr:gt(0)').remove();
      //       for (var i = 0; i < data.topics.length; i++) {
      //         if (data.canPinLock.includes(data.topics[i].id)) {
      //           appendTopicAdmin(data.topics[i]);
      //         } else {
      //           appendTopicGuest(data.topics[i]);
      //         }
      //       }
      //
      //     });
      //
      //   } else if (role === "registered") {
      //     var req = {
      //       "forum_id": forumParams.get('id'),
      //       "topic_page": topic_page,
      //       "topic_results": topic_results
      //     }
      //
      //     $.get("TopicServlet", req, function(data) {
      //       page_topics.find('tr:gt(0)').remove();
      //       if (data.status === "failure") {
      //         p_message.text(data.message)
      //         notification_window.show().delay(2000).fadeOut();
      //       } else {
      //         for (var i = 0; i < data.length; i++) {
      //           appendTopicGuest(data[i]);
      //         }
      //       }
      //
      //     });
      //
      //   }else
      //   {
      //     var req = {
      //       "forum_id": forumParams.get('id'),
      //       "topic_page": topic_page,
      //       "topic_results": topic_results
      //     }
      //
      //     $.get("TopicServlet", req, function(data) {
      //       page_topics.find('tr:gt(0)').remove();
      //       if (data.status === "failure") {
      //         p_message.text(data.message)
      //         notification_window.show().delay(2000).fadeOut();
      //       } else {
      //         for (var i = 0; i < data.length; i++) {
      //           appendTopicGuest(data[i]);
      //         }
      //       }
      //
      //     });
      //
      //   }
      //
      // }







});
