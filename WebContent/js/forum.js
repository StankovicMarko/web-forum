$(document).ready(function() {

  let forumParams = new URLSearchParams(window.location.search);
  if (!forumParams.has('id')) {
    window.location.replace("index.html");
  } else {

    var parent_forum = $("#parent_forum");
    var get_parent_id_req = {
      "forum_id": forumParams.get('id'),
      "get_parent_forum": "yes"
    }
    $.get("UtilServlet", get_parent_id_req, function(data) {
      if (data != 0) {
        parent_forum.append('<a href="forum.html?id=' + data + '">Back (to parent)</a>');
      }
    });


    var p_message = $("#message");
    var notification_window = $("#notification_window");

    var search_forum_button = $("#big_search");
    var search_forum_submit = $("#search_forum_submit");
    var search_forum_close = $("#search_forum_close");
    var search_forum_window = $(".search_forum_window");

    var forum_page_html = $("#forum_page");
    var forum_page = 1
    // parseInt(forum_page_html.text());

    var forum_results_html = $("#forum_results");
    var forum_results = parseInt(forum_results_html.find(":selected").text());
    var forum_back = $("#forum_back");
    var forum_next = $("#forum_next");

    var topic_page_html = $("#topic_page");
    var topic_page = 1
    var topic_results_html = $("#topic_results");
    var topic_results = parseInt(topic_results_html.find(":selected").text());
    var topic_back = $("#topic_back");
    var topic_next = $("#topic_next");
    var page_topics = $("#page_topics");

    var logged_in = $("#logged_in");
    var logged_out = $("#logged_out");
    var logged_user = $("#logged_user");
    var admin_options = $(".admin_options");

    var page_forums = $("#page_forums");

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


    $.get("LoginServlet", function(data) {
      if (data.status === "yes") {
        //dodaj handlere za pannel i search kad je ulogovan
        logged_out.hide();
        logged_user.text(data.username);
        logged_in.show();
        if (data.role === "admin") {
          adminInterface();
          getForums("admin");
          getTopics("admin");

        } else if (data.role === "registered") {
          registeredInterface();
          getForums("registered");
          getTopics("registered");
        } else if (data.role === "moderator") {
          moderatorInterface();
          getForums("moderator");
          getTopics("moderator");
        } ////else if neka druga uloga radi drugi posao

      } else {

        getForums("guest");
        getTopics("guest");

        ///pagination forums next for guest
        $(forum_next).click(function(e) {
          e.stopPropagation();
          forum_page++;
          forum_page_html.text(forum_page);
          getForums("guest");
        });

        $(forum_back).click(function(e) {
          e.stopPropagation();
          if (forum_page > 1) {
            forum_back.prop("disabled", false);
            forum_page--;
            forum_page_html.text(forum_page);
            getForums("guest");
          }
        });

        $('.pagination').on('change', '#forum_results', function() {
          forum_results = parseInt(forum_results_html.find(":selected").text());
          forum_page = 1;
          forum_page_html.text(forum_page);
          getForums("guest");
        });

        ///pagination topics
        $(topic_next).click(function(e) {
          e.stopPropagation();
          topic_page++;
          topic_page_html.text(topic_page);
          getTopics("guest");
        });

        $(topic_back).click(function(e) {
          e.stopPropagation();
          if (topic_page > 1) {
            topic_back.prop("disabled", false);
            topic_page--;
            topic_page_html.text(topic_page);
            getTopics("guest");
          }
        });

        $('.pagination').on('change', '#topic_results', function() {
          topic_results = parseInt(topic_results_html.find(":selected").text());
          topic_page = 1;
          topic_page_html.text(topic_page);
          getTopics("guest");
        });


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
            search_forum_window.hide();

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
            search_forum_window.hide();
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
                  resetPaganationHandlers();
                  adminInterface();
                  getForums(data.role);
                  getTopics(data.role);
                } else if (data.role === "registered") {
                  resetPaganationHandlers();
                  registeredInterface();
                  //ver cu zvati regi interface da stavi handler na pagi
                  getForums(data.role);
                  getTopics(data.role);
                } else if (data.role === "moderator") {
                  resetPaganationHandlers();
                  moderatorInterface();
                  //var cu zvati interface njihov
                  getForums(data.role);
                  getTopics(data.role);
                }

              }
            });
          }
        });


        function resetPaganationHandlers() {
          forum_next.off();
          forum_back.off();
          forum_page_html.text("1");
          forum_page = 1;
          forum_results_html.val("5"); /// select option automatically
          forum_results = 5;


          ///for topics
          topic_next.off();
          topic_back.off();
          topic_page_html.text("1");
          topic_page = 1;
          topic_results_html.val("5"); /// select option automatically
          topic_results = 5;

        }



      } ///nije ulogovan kraj

      function adminInterface() {


        var get_user_status = {
          "username": logged_user.text(),
          "get_user_status": "yes"
        }
        $.get("UtilServlet", get_user_status, function(banned) {
          if (!banned) {
        admin_options.show();

        var add_forum = $("#add_forum");
        var add_forum_window = $(".add_forum_window");
        var add_forum_submit = $("#add_forum_submit");
        var add_forum_close = $("#add_forum_close");

        var add_forum_type = $("#add_forum_type");
        var parentForumType = "";

        var req = {
          "forum_id": forumParams.get('id'),
          "get_parent_type": "yes"
        }

        $.get("UtilServlet", req, function(data) {
          parentForumType = data;
          // console.log(data);
          // console.log(parentForumType);
        });

        var add_forum_name = $("#add_forum_name");
        var add_forum_description = $("#add_forum_description");

        var edit_forum_window = $(".edit_forum_window");
        var edit_forum_submit = $("#edit_forum_submit");
        var edit_forum_close = $("#edit_forum_close");
        var edit_forum_name = $("#edit_forum_name");
        var edit_forum_description = $("#edit_forum_description");

        var add_topic = $("#add_topic");
        var add_topic_window = $(".add_topic_window");
        var add_topic_submit = $("#add_topic_submit");
        var add_topic_close = $("#add_topic_close");
        var add_topic_name = $("#add_topic_name");
        var add_topic_description = $("#add_topic_description");
        var add_topic_content = $("#add_topic_content");

        $(add_topic).click(function(e) {
          e.stopPropagation();
          add_topic_window.show();
          add_forum_window.hide();
        });

        $(add_topic_submit).click(function(e) {
          e.stopPropagation();
          var tn = add_topic_name.val();
          var td = add_topic_description.val();
          var tc = add_topic_content.val();
          var owner = logged_user.text();
          var action = "create";
          var parentForum = forumParams.get('id');

          if (tn === "" || tc === "") {
            p_message.text("* Marked fields must be filled")
            notification_window.show().delay(2000).fadeOut();
          } else {

            var jsonNewTopic = {
              'action': action,
              'owner': owner,
              'name': tn,
              'parentId': parentForum,
              'description': td,
              'content': tc

            };

            // console.log(jsonNewTopic);

            $.post('TopicServlet', jsonNewTopic, function(data) {
              p_message.text(data.message);
              notification_window.show().delay(2000).fadeOut();
              if (data.status === "success") {
                window.location.replace("forum.html?id=" + forumParams.get("id"));

              }
            });

          }

        });




        $(add_topic_close).click(function(e) {
          e.stopPropagation();

          resetAddTopicInput();
          add_topic_window.hide();
        });


        page_topics.on('click', 'button.lock_topic', function(e) {
          e.preventDefault();
          if (confirm("Are you sure you want to lock/unlock this topic?")) {
            var lock_btn = $(this);
            var topic_id = lock_btn.attr('topic_id');
            var status = lock_btn.attr('topic_status');
            var row = lock_btn.closest('tr'); //najblizi njemu jer taj tr sadrzi bas taj input
            var status_td = row.find("td.status");
            var pinned_btn = row.find(".pin_topic");
            var pinned = pinned_btn.attr("topic_pinned");

            var req;
            if (status == "true") {
              req = {
                'action': 'unlock',
                'topic_id': topic_id
              }

            } else {
              req = {
                'action': 'lock',
                'topic_id': topic_id
              }

            }

            $.post('TopicServlet', req, function(data) {
              if (data.message) {
                p_message.text(data.message)
                notification_window.show().delay(2000).fadeOut();
              }
              if (data.status === 'success') {
                if (status == "true" && pinned == "true") {
                  status_td.html(topicStatus(false, true));
                  lock_btn.attr("topic_status", "false");
                  // pinned_btn.attr("topic_pinned", "false");
                } else if (status == "true" && pinned == "false") {
                  status_td.html(topicStatus(false, false));
                  lock_btn.attr("topic_status", "false");
                  // pinned_btn.attr("topic_pinned", "true");
                } else if (status == "false" && pinned == "true") {
                  status_td.html(topicStatus(true, true));
                  lock_btn.attr("topic_status", "true");
                  // pinned_btn.attr("topic_pinned", "false");
                } else if (status == "false" && pinned == "false") {
                  status_td.html(topicStatus(true, false));
                  lock_btn.attr("topic_status", "true");
                  // pinned_btn.attr("topic_pinned", "true");
                }


              }

            });

            // return false;

          }
        });


        page_topics.on('click', 'button.pin_topic', function(e) {
          e.preventDefault();
          if (confirm("Are you sure you want to pin/unpin this topic?")) {
            var pin_btn = $(this);
            var topic_id = pin_btn.attr('topic_id');
            var pinned = pin_btn.attr('topic_pinned');
            var row = pin_btn.closest('tr'); //najblizi njemu jer taj tr sadrzi bas taj input
            var status_td = row.find("td.status");
            var lock_btn = row.find(".lock_topic");
            var status = lock_btn.attr("topic_status");

            var req;
            if (pinned == "true") {
              req = {
                'action': 'unpin',
                'topic_id': topic_id
              }

            } else {
              req = {
                'action': 'pin',
                'topic_id': topic_id
              }

            }

            $.post('TopicServlet', req, function(data) {
              if (data.message) {
                p_message.text(data.message)
                notification_window.show().delay(2000).fadeOut();
              }
              if (data.status === 'success') {
                window.location.replace("forum.html?id=" + forumParams.get('id'));
              }
            });

            // return false;

          }
        });


        $(add_forum).click(function(e) {
          e.stopPropagation();
          add_topic_window.hide();
          add_forum_window.show();
          if (parentForumType === "" || parentForumType === "Public") {
            add_forum_type.html('<option value="Public">Public</option><option value="Open">Open</option><option value="Closed">Closed</option>');
          } else if (parentForumType === "Open") {
            add_forum_type.html('<option value="Open">Open</option><option value="Closed">Closed</option>');
          } else {
            add_forum_type.html('<option value="Closed">Closed</option>');
          }
        });

        $(add_forum_submit).click(function(e) {
          e.stopPropagation();
          var fn = add_forum_name.val();
          var fd = add_forum_description.val();
          var type = $('#add_forum_type').find(":selected").text();
          var owner = logged_user.text();
          var action = "create";
          var parentForum = forumParams.get('id');

          if (fn === "" || type === "") {
            p_message.text("* Marked fields must be filled")
            notification_window.show().delay(2000).fadeOut();
            //  console.log(fn, fd, type, owner, action, parentForum);
          } else {

            var jsonNewForum = {
              'action': action,
              'owner': owner,
              'name': fn,
              'parentId': parentForum,
              'description': fd,
              'type': type

            };

            // console.log(jsonNewForum);

            $.post('ForumServlet', jsonNewForum, function(data) {
              p_message.text(data.message);
              notification_window.show().delay(2000).fadeOut();
              if (data.status === "success") {
                window.location.replace("forum.html?id=" + forumParams.get("id"));
                // resetAddForumInput();
                // add_forum_window.hide();
                // appendForumAdmin(data.forum);

              }
            });

          }

        });

        $(add_forum_close).click(function(e) {
          e.stopPropagation();
          resetAddForumInput();
          add_forum_window.hide();
        });

        $(edit_forum_close).click(function(e) {
          e.stopPropagation();
          resetEditForumInput();
          edit_forum_window.hide();
        });

        function resetAddForumInput() {
          add_forum_name.val("");
          add_forum_description.val("");
        }

        function resetAddTopicInput() {
          add_topic_name.val("");
          add_topic_description.val("");
          add_topic_content.val("");
        }

        function resetEditForumInput() {
          edit_forum_name.val("");
          edit_forum_description.val("");
        }

        page_forums.on('click', 'button.lock_forum', function(e) {
          e.preventDefault();
          if (confirm("Are you sure you want to lock/unlock this forum?")) {
            var lock_btn = $(this);
            var forum_id = lock_btn.attr('forum_id');
            var status = lock_btn.attr('forum_status');
            var row = lock_btn.closest('tr'); //najblizi njemu jer taj tr sadrzi bas taj input
            var status_td = row.find("td.status");


            var req;
            if (status == "true") {
              req = {
                'action': 'unlock',
                'forum_id': forum_id
              }

            } else {
              req = {
                'action': 'lock',
                'forum_id': forum_id
              }

            }

            $.post('ForumServlet', req, function(data) {
              if (data.message) {
                p_message.text(data.message)
                notification_window.show().delay(2000).fadeOut();
              }
              if (data.status === 'success') {
                if (status == "true") {
                  status_td.html(forumStatus(false));
                  lock_btn.attr("forum_status", "false");
                } else {
                  status_td.html(forumStatus(true));
                  lock_btn.attr("forum_status", "true");
                }

              }

            });

            // return false;

          }
        });

        page_forums.on('click', 'button.edit_forum', function(e) {
          e.preventDefault();
          var forum_id = $(this).attr('forum_id');
          var row = $(this).closest('tr'); //najblizi njemu jer taj tr sadrzi bas taj input
          var f_n = row.find(":eq(0)").text();
          var f_d = row.find(":eq(0)").find(":eq(0)").attr("title");
          var edit_forum_type = $('#edit_forum_type');
          // var f_t = row.find(":eq(17)").text();

          edit_forum_window.show();
          edit_forum_name.val(f_n);
          edit_forum_description.val(f_d);


          if (parentForumType === "" || parentForumType === "Public") {
            edit_forum_type.html('<option value="Public">Public</option><option value="Open">Open</option><option value="Closed">Closed</option>');
          } else if (parentForumType === "Open") {
            edit_forum_type.html('<option value="Open">Open</option><option value="Closed">Closed</option>');
          } else {
            edit_forum_type.html('<option value="Closed">Closed</option>');
          }

          $(edit_forum_submit).click(function(e) {
            e.stopPropagation();
            var fn = edit_forum_name.val();
            var fd = edit_forum_description.val();
            var type = edit_forum_type.find(":selected").text();
            // var owner = logged_user.text();
            var action = "edit";
            // var parentForum = 0;

            if (fn === "" || type === "") {
              p_message.text("* Marked fields must be filled")
              notification_window.show().delay(2000).fadeOut();
              //  console.log(fn, fd, type, owner, action, parentForum);
            } else {

              var jsonNewForum = {
                'action': action,
                'forum_id': forum_id,
                'name': fn,
                'description': fd,
                'type': type
              };

              // console.log(jsonNewForum);

              $.post('ForumServlet', jsonNewForum, function(data) {
                if (data.status === "success") {
                  window.location.replace("forum.html?id=" + forumParams.get("id"));
                } else {
                  p_message.text(data.message);
                  notification_window.show().delay(2000).fadeOut();
                }
              });

            }

          });
        });

        page_forums.on('click', 'button.delete_forum', function(e) {
          e.preventDefault();
          if (confirm("Are you sure you want to delete this forum?")) {
            var forum_id = $(this).attr('forum_id');
            var row = $(this).closest('tr'); //najblizi njemu jer taj tr sadrzi bas taj input

            $.post('ForumServlet', {
              'action': 'delete',
              'forum_id': forum_id
            }, function(data) {
              if (data.message) {
                p_message.text(data.message)
                notification_window.show().delay(2000).fadeOut();
              }
              if (data.status === 'success') {
                row.remove();
              }

            });
          }
        });


      }
    });

        $(forum_next).click(function(e) {
          e.stopPropagation();
          forum_page++;
          forum_page_html.text(forum_page);
          getForums("admin");
        });

        $(forum_back).click(function(e) {
          e.stopPropagation();
          if (forum_page > 1) {
            forum_back.prop("disabled", false);
            forum_page--;
            forum_page_html.text(forum_page);
            getForums("admin");
          }
        });

        $('.pagination').on('change', '#forum_results', function() {
          forum_results = parseInt(forum_results_html.find(":selected").text());
          forum_page = 1;
          forum_page_html.text(forum_page);
          getForums("admin");
        });


        $(topic_next).click(function(e) {
          e.stopPropagation();
          topic_page++;
          topic_page_html.text(topic_page);
          getTopics("admin");
        });

        $(topic_back).click(function(e) {
          e.stopPropagation();
          if (topic_page > 1) {
            topic_back.prop("disabled", false);
            topic_page--;
            topic_page_html.text(topic_page);
            getTopics("admin");
          }
        });

        $('.pagination').on('change', '#topic_results', function() {
          topic_results = parseInt(topic_results_html.find(":selected").text());
          topic_page = 1;
          topic_page_html.text(topic_page);
          getTopics("admin");
        });





      } ///end of admin interface



      function moderatorInterface() {

        var get_forum_status = {
          "username": logged_user.text(),
          "get_user_status": "yes"
        }
        $.get("UtilServlet", get_forum_status, function(banned) {
          if (!banned) {

        var moderator_options = $(".moderator_options").show();
        var add_topic = $("#add_topic_moder");
        var add_topic_window = $(".add_topic_window");
        var add_topic_submit = $("#add_topic_submit");
        var add_topic_close = $("#add_topic_close");
        var add_topic_name = $("#add_topic_name");
        var add_topic_description = $("#add_topic_description");
        var add_topic_content = $("#add_topic_content");

        $(add_topic).click(function(e) {
          e.stopPropagation();
          add_topic_window.show();
        });

        $(add_topic_submit).click(function(e) {
          e.stopPropagation();
          var tn = add_topic_name.val();
          var td = add_topic_description.val();
          var tc = add_topic_content.val();
          var owner = logged_user.text();
          var action = "create";
          var parentForum = forumParams.get('id');

          if (tn === "" || tc === "") {
            p_message.text("* Marked fields must be filled")
            notification_window.show().delay(2000).fadeOut();
          } else {

            var jsonNewTopic = {
              'action': action,
              'owner': owner,
              'name': tn,
              'parentId': parentForum,
              'description': td,
              'content': tc

            };

            $.post('TopicServlet', jsonNewTopic, function(data) {
              p_message.text(data.message);
              notification_window.show().delay(2000).fadeOut();
              if (data.status === "success") {
                window.location.replace("forum.html?id=" + forumParams.get("id"));

              }
            });

          }

        });

        page_topics.on('click', 'button.lock_topic', function(e) {
          e.preventDefault();
          if (confirm("Are you sure you want to lock/unlock this topic?")) {
            var lock_btn = $(this);
            var topic_id = lock_btn.attr('topic_id');
            var status = lock_btn.attr('topic_status');
            var row = lock_btn.closest('tr'); //najblizi njemu jer taj tr sadrzi bas taj input
            var status_td = row.find("td.status");
            var pinned_btn = row.find(".pin_topic");
            var pinned = pinned_btn.attr("topic_pinned");

            var req;
            if (status == "true") {
              req = {
                'action': 'unlock',
                'topic_id': topic_id
              }

            } else {
              req = {
                'action': 'lock',
                'topic_id': topic_id
              }

            }

            // console.log(req);
            $.post('TopicServlet', req, function(data) {
              if (data.message) {
                p_message.text(data.message)
                notification_window.show().delay(2000).fadeOut();
              }
              if (data.status === 'success') {
                if (status == "true" && pinned == "true") {
                  status_td.html(topicStatus(false, true));
                  lock_btn.attr("topic_status", "false");
                  // pinned_btn.attr("topic_pinned", "false");
                } else if (status == "true" && pinned == "false") {
                  status_td.html(topicStatus(false, false));
                  lock_btn.attr("topic_status", "false");
                  // pinned_btn.attr("topic_pinned", "true");
                } else if (status == "false" && pinned == "true") {
                  status_td.html(topicStatus(true, true));
                  lock_btn.attr("topic_status", "true");
                  // pinned_btn.attr("topic_pinned", "false");
                } else if (status == "false" && pinned == "false") {
                  status_td.html(topicStatus(true, false));
                  lock_btn.attr("topic_status", "true");
                  // pinned_btn.attr("topic_pinned", "true");
                }


              }

            });

            // return false;

          }
        });


        page_topics.on('click', 'button.pin_topic', function(e) {
          e.preventDefault();
          if (confirm("Are you sure you want to pin/unpin this topic?")) {
            var pin_btn = $(this);
            var topic_id = pin_btn.attr('topic_id');
            var pinned = pin_btn.attr('topic_pinned');
            var row = pin_btn.closest('tr'); //najblizi njemu jer taj tr sadrzi bas taj input
            var status_td = row.find("td.status");
            var lock_btn = row.find(".lock_topic");
            var status = lock_btn.attr("topic_status");

            var req;
            if (pinned == "true") {
              req = {
                'action': 'unpin',
                'topic_id': topic_id
              }

            } else {
              req = {
                'action': 'pin',
                'topic_id': topic_id
              }

            }

            $.post('TopicServlet', req, function(data) {
              if (data.message) {
                p_message.text(data.message)
                notification_window.show().delay(2000).fadeOut();
              }
              if (data.status === 'success') {
                window.location.replace("forum.html?id=" + forumParams.get('id'));
              }
            });

            // return false;

          }
        });


        $(add_topic_close).click(function(e) {
          e.stopPropagation();
          resetAddTopicInput();
          add_topic_window.hide();
        });

        function resetAddTopicInput() {
          add_topic_name.val("");
          add_topic_description.val("");
          add_topic_content.val("");
        }

      }
    });

        $(forum_next).click(function(e) {
          e.stopPropagation();
          forum_page++;
          forum_page_html.text(forum_page);
          getForums("guest");
        });

        $(forum_back).click(function(e) {
          e.stopPropagation();
          if (forum_page > 1) {
            forum_back.prop("disabled", false);
            forum_page--;
            forum_page_html.text(forum_page);
            getForums("guest");
          }
        });

        $('.pagination').on('change', '#forum_results', function() {
          forum_results = parseInt(forum_results_html.find(":selected").text());
          forum_page = 1;
          forum_page_html.text(forum_page);
          getForums("guest");
        });


        $(topic_next).click(function(e) {
          e.stopPropagation();
          topic_page++;
          topic_page_html.text(topic_page);
          getTopics("moderator");
        });

        $(topic_back).click(function(e) {
          e.stopPropagation();
          if (topic_page > 1) {
            topic_back.prop("disabled", false);
            topic_page--;
            topic_page_html.text(topic_page);
            getTopics("moderator");
          }
        });

        $('.pagination').on('change', '#topic_results', function() {
          topic_results = parseInt(topic_results_html.find(":selected").text());
          topic_page = 1;
          topic_page_html.text(topic_page);
          getTopics("moderator");
        });

      }

      function registeredInterface() {
        // regis
        var regi_add_topic = $("#add_topic_regi");
        var add_topic;
        var add_topic_window;
        var add_topic_submit;
        var add_topic_close;
        var add_topic_name;
        var add_topic_description;
        var add_topic_content;

        var get_forum_status = {
          "forum_id": forumParams.get('id'),
          "get_forum_status": "yes"
        }
        $.get("UtilServlet", get_forum_status, function(data) {
          if (!data) {
            regi_add_topic.show();
            add_topic = $("#add_topic_regi");
            add_topic_window = $(".add_topic_window");
            add_topic_submit = $("#add_topic_submit");
            add_topic_close = $("#add_topic_close");
            add_topic_name = $("#add_topic_name");
            add_topic_description = $("#add_topic_description");
            add_topic_content = $("#add_topic_content");

            $(add_topic).click(function(e) {
              e.stopPropagation();
              add_topic_window.show();
            });

            $(add_topic_submit).click(function(e) {
              e.stopPropagation();
              var tn = add_topic_name.val();
              var td = add_topic_description.val();
              var tc = add_topic_content.val();
              var owner = logged_user.text();
              var action = "create";
              var parentForum = forumParams.get('id');

              if (tn === "" || tc === "") {
                p_message.text("* Marked fields must be filled")
                notification_window.show().delay(2000).fadeOut();
              } else {

                var jsonNewTopic = {
                  'action': action,
                  'owner': owner,
                  'name': tn,
                  'parentId': parentForum,
                  'description': td,
                  'content': tc

                };

                $.post('TopicServlet', jsonNewTopic, function(data) {
                  p_message.text(data.message);
                  notification_window.show().delay(2000).fadeOut();
                  if (data.status === "success") {
                    window.location.replace("forum.html?id=" + forumParams.get("id"));

                  }
                });

              }

            });

            $(add_topic_close).click(function(e) {
              e.stopPropagation();
              resetAddTopicInput();
              add_topic_window.hide();
            });

            function resetAddTopicInput() {
              add_topic_name.val("");
              add_topic_description.val("");
              add_topic_content.val("");
            }

          }
        });




        $(forum_next).click(function(e) {
          e.stopPropagation();
          forum_page++;
          forum_page_html.text(forum_page);
          getForums("guest");
        });

        $(forum_back).click(function(e) {
          e.stopPropagation();
          if (forum_page > 1) {
            forum_back.prop("disabled", false);
            forum_page--;
            forum_page_html.text(forum_page);
            getForums("guest");
          }
        });

        $('.pagination').on('change', '#forum_results', function() {
          forum_results = parseInt(forum_results_html.find(":selected").text());
          forum_page = 1;
          forum_page_html.text(forum_page);
          getForums("guest");
        });


        $(topic_next).click(function(e) {
          e.stopPropagation();
          topic_page++;
          topic_page_html.text(topic_page);
          getTopics("registered");
        });

        $(topic_back).click(function(e) {
          e.stopPropagation();
          if (topic_page > 1) {
            topic_back.prop("disabled", false);
            topic_page--;
            topic_page_html.text(topic_page);
            getTopics("registered");
          }
        });

        $('.pagination').on('change', '#topic_results', function() {
          topic_results = parseInt(topic_results_html.find(":selected").text());
          topic_page = 1;
          topic_page_html.text(topic_page);
          getTopics("registered");
        });

      }

      function forumStatus(boolean) {
        if (boolean) {
          return '<span class="glyphicon glyphicon-lock" style="display: inline;" title="Locked"></span>';
        } else {
          return '<span class="glyphicon glyphicon-ok" style="display: inline;" title="Unlocked"></span>';
        }

      }

      function appendForumAdmin(forum) {
        // console.log(typeof(forum.locked));
        // console.log(parent_forum.html());
        //
        //   parent_forum.html('<a href="forum.html?id='+forum.parentForumId+'">Parent Forum</a>');

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
          '<td>' +
          '<form style="display:inline">' +
          '<button class="no_button lock_forum" type="submit" value="lock"' +
          'forum_status="' + forum.locked.toString() + '"' +
          'forum_id="' + forum.id + '">' +
          '<span class="glyphicon glyphicon-lock"' +
          'title="Lock/Unlock This Forum"></span> </button>' +
          '		</form>' +
          '<form style="display:inline">' +
          '<button class="no_button edit_forum" type="submit" value="edit"' +
          'forum_status="' + forum.locked.toString() + '"' +
          'forum_id="' + forum.id + '">' +
          '<span class="glyphicon glyphicon-edit"' +
          'title="Edit This Forum"></span> </button>' +
          '		</form>' +
          '<form style="display:inline">' +
          '<button class="no_button delete_forum" type="submit" value="delete"' +
          'forum_status="' + forum.locked.toString() + '"' +
          'forum_id="' + forum.id + '">' +
          '<span class="glyphicon glyphicon-remove"' +
          'title="Remove This Forum"></span> </button>' +
          '		</form>' +
          '<p>' + forum.type +
          '</p>' +
          '  </td>' +
          '  </tr>'
        );

      }

      function appendForumGuest(forum) {
        // if(parent_forum.html()!= ""){
        //   parent_forum.html('<a href="forum.html?id='+forum.parentForumId+'">Parent Forum</a>');
        // }
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


      function appendForumDetails(details) {
        //lista pa mora ovako
        var forum_details = $("#forum_details");
        forum_details.find('tr:gt(0)').remove();
        forum_details.append(
          '<tr>' +
          '<td>Name</td>' +
          '<td> ' + details[0] + '</td>' + //name
          '</tr>' +
          '<tr>' +
          '<td>Description</td>' +
          '<td>' + details[1] + '</td>' + //description
          '</tr>' +
          '<tr>' +
          '<td>Owner</td>' +
          '<td><a href="user.html?username=' + details[2] + '">' + //username
          details[2] +
          '</a></td>' +
          '</tr>' +
          '<tr>' +
          '<td>Date Created</td>' +
          '<td>' + details[3] + '</td>' + ///date
          '</tr>'


        )
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

        } else if (role === "moderator") {
          var req = {
            "forum_id": forumParams.get('id'),
            "forum_page": forum_page,
            "forum_results": forum_results
          }

          $.get("ForumServlet", req, function(data) {
            page_forums.find('tr:gt(0)').remove();
            for (var i = 0; i < data.forums.length; i++) {
              appendForumGuest(data.forums[i]);
            }
            appendForumDetails(data.details);

          });

        } else if (role === "registered") {
          var req = {
            "forum_id": forumParams.get('id'),
            "forum_page": forum_page,
            "forum_results": forum_results
          }

          $.get("ForumServlet", req, function(data) {
            page_forums.find('tr:gt(0)').remove();
            if (data.status === "failure") {
              p_message.text(data.message)
              notification_window.show().delay(2000).fadeOut();
            } else {
              for (var i = 0; i < data.forums.length; i++) {
                appendForumGuest(data.forums[i]);
              }
            }
            appendForumDetails(data.details);

          });

        } else {
          var req = {
            "forum_id": forumParams.get('id'),
            "forum_page": forum_page,
            "forum_results": forum_results
          }

          $.get("ForumServlet", req, function(data) {
            page_forums.find('tr:gt(0)').remove();
            // console.log(data.details);
            if (data.status === "failure") {
              p_message.text(data.message)
              notification_window.show().delay(2000).fadeOut();
            } else {
              for (var i = 0; i < data.forums.length; i++) {
                appendForumGuest(data.forums[i]);
              }
            }
            appendForumDetails(data.details);

          });
        }

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

      function appendTopicAdmin(topic) {
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
          '<td>' +
          '<form style="display:inline">' +
          '<button class="no_button pin_topic" type="submit" value="pin"' +
          'topic_pinned="' + topic.pinned.toString() + '"' +
          'topic_id="' + topic.id + '">' +
          '<span class="glyphicon glyphicon-flag"' +
          'title="Pin/Unpin This Topic"></span> </button>' +
          '		</form>' +
          '<form style="display:inline">' +
          '<button class="no_button lock_topic" type="submit" value="lock"' +
          'topic_status="' + topic.locked.toString() + '"' +
          'topic_id="' + topic.id + '">' +
          '<span class="glyphicon glyphicon-lock"' +
          'title="Lock/Unlock This Topic"></span> </button>' +
          '		</form>' +
          '  </td>' +
          '  </tr>'
        );

      }


      function appendTopicGuest(topic) {
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




      function getTopics(role) {
        if (role === "admin") {
          var req = {
            "forum_id": forumParams.get('id'),
            "topic_page": topic_page,
            "topic_results": topic_results
          }

          $.get("TopicServlet", req, function(data) {
            page_topics.find('tr:gt(0)').remove();
            for (var i = 0; i < data.length; i++) {
              appendTopicAdmin(data[i]);
            }

          });

        } else if (role === "moderator") {
          var req = {
            "forum_id": forumParams.get('id'),
            "topic_page": topic_page,
            "topic_results": topic_results
          }

          $.get("TopicServlet", req, function(data) {
            page_topics.find('tr:gt(0)').remove();
            for (var i = 0; i < data.topics.length; i++) {
              if (data.canPinLock.includes(data.topics[i].id)) {
                appendTopicAdmin(data.topics[i]);
              } else {
                appendTopicGuest(data.topics[i]);
              }
            }

          });

        } else if (role === "registered") {
          var req = {
            "forum_id": forumParams.get('id'),
            "topic_page": topic_page,
            "topic_results": topic_results
          }

          $.get("TopicServlet", req, function(data) {
            page_topics.find('tr:gt(0)').remove();
            if (data.status === "failure") {
              p_message.text(data.message)
              notification_window.show().delay(2000).fadeOut();
            } else {
              for (var i = 0; i < data.length; i++) {
                appendTopicGuest(data[i]);
              }
            }

          });

        }else
        {
          var req = {
            "forum_id": forumParams.get('id'),
            "topic_page": topic_page,
            "topic_results": topic_results
          }

          $.get("TopicServlet", req, function(data) {
            page_topics.find('tr:gt(0)').remove();
            if (data.status === "failure") {
              p_message.text(data.message)
              notification_window.show().delay(2000).fadeOut();
            } else {
              for (var i = 0; i < data.length; i++) {
                appendTopicGuest(data[i]);
              }
            }

          });

        }

      }




    });

  }
});
