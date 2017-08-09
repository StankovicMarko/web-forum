$(document).ready(function() {

  let topicParams = new URLSearchParams(window.location.search);
  if (!topicParams.has('id')) {
    window.location.replace("index.html");
  } else {

    var parent_forum_id;
    var parent_forum = $("#parent_forum");
    var get_parent_id_req = {
      "topic_id": topicParams.get('id'),
      "get_parent_forum": "yes"
    }
    $.get("UtilServlet", get_parent_id_req, function(data) {
      if (data != 0) {
        parent_forum_id = data;
        parent_forum.append('<a href="forum.html?id=' + data + '">Back (to parent)</a>');
      }
    });


    var p_message = $("#message");
    var notification_window = $("#notification_window");

    var search_forum_button = $("#big_search");

    var reply_page_html = $("#reply_page");
    var reply_page = 1;
    // parseInt(forum_page_html.text());

    var reply_results_html = $("#reply_results");
    var reply_results = parseInt(reply_results_html.find(":selected").text());
    var reply_back = $("#reply_back");
    var reply_next = $("#reply_next");
    var topic_replies = $("#topic_replies");

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


    $.get("LoginServlet", function(data) {
      if (data.status === "yes") {
        //dodaj handlere za pannel i search kad je ulogovan
        logged_out.hide();
        logged_user.text(data.username);
        logged_in.show();
        if (data.role === "admin") {
          adminInterface();
          getReplies("admin");
        } else if (data.role === "registered") {
          registeredInterface();
          getReplies("registered");
        } else if (data.role === "moderator") {
          getReplies("moderator");
          moderatorInterface();

        } ////else if neka druga uloga radi drugi posao

      } else {

        getReplies("guest");

        ///pagination reply for guest
        $(reply_next).click(function(e) {
          e.stopPropagation();
          reply_page++;
          reply_page_html.text(reply_page);
          getReplies("guest");
        });

        $(reply_back).click(function(e) {
          e.stopPropagation();
          if (reply_page > 1) {
            reply_back.prop("disabled", false);
            reply_page--;
            reply_page_html.text(reply_page);
            // getForums("admin");
            getReplies("guest");
          }
        });

        $('.pagination').on('change', '#reply_results', function() {
          reply_results = parseInt(reply_results_html.find(":selected").text());
          reply_page = 1;
          reply_page_html.text(reply_page);
          // getForums("admin");
          getReplies("guest");
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
                  getReplies("admin");
                  // getForums(data.role);
                  // getTopics(data.role);
                } else if (data.role === "registered") {
                  resetPaganationHandlers();
                  registeredInterface();
                  getReplies("registered");
                  //ver cu zvati regi interface da stavi handler na pagi
                  // getForums(data.role);
                  // getTopics(data.role);
                } else if (data.role === "moderator") {
                  resetPaganationHandlers();
                  getReplies("moderator");
                  moderatorInterface();

                  //var cu zvati interface njihov
                  // getForums(data.role);
                  // getTopics(data.role);
                }

              }
            });
          }
        });




        function resetPaganationHandlers() {
          reply_next.off();
          reply_back.off();
          reply_page_html.text("1");
          reply_page = 1;
          reply_results_html.val("5"); /// select option automatically
          reply_results = 5;

        }



      } ///nije ulogovan kraj


      function registeredInterface() {

        var get_canEditDelete = {
          "topic_id": topicParams.get('id'),
          "parent_forum_id": parent_forum_id,
          "get_canEditDelete": "yes"
        }

        $.get("UtilServlet", get_canEditDelete, function(data) {
          if (data == true) {
            var registered_options = $(".registered_options");
            registered_options.show();
            var edit_topic = $("#edit_topic_registered");
            var edit_topic_window = $(".edit_topic_window");
            var edit_topic_submit = $("#edit_topic_submit");
            var edit_topic_close = $("#edit_topic_close");
            var edit_topic_name = $("#edit_topic_name");
            var edit_topic_description = $("#edit_topic_description");
            var edit_topic_content = $("#edit_topic_content");

            $(edit_topic).click(function(e) {
              e.preventDefault();
              e.stopPropagation();
              edit_topic_window.show();
              edit_topic_name.val($("#topic_name").text());
              edit_topic_description.val($("#topic_description").text());
              edit_topic_content.val($("#topic_content").val());
            });

            $(edit_topic_submit).click(function(e) {
              e.stopPropagation();
              var tn = edit_topic_name.val();
              var td = edit_topic_description.val();
              var tc = edit_topic_content.val();
              var action = "edit";
              var topic_id = topicParams.get('id');

              if (tn === "" || tc === "") {
                p_message.text("* Marked fields must be filled")
                notification_window.show().delay(2000).fadeOut();
              } else {

                var jsonEditTopic = {
                  'action': action,
                  'topic_id': topic_id,
                  'name': tn,
                  'description': td,
                  'content': tc

                };

                $.post('TopicServlet', jsonEditTopic, function(data) {
                  p_message.text(data.message);
                  notification_window.show().delay(2000).fadeOut();
                  if (data.status === "success") {
                    window.location.replace("topic.html?id=" + topicParams.get("id"));
                  }
                });
              }
            });

            $(edit_topic_close).click(function(e) {
              e.stopPropagation();
              resetEditTopicInput();
              edit_topic_window.hide();
            });

            function resetEditTopicInput() {
              edit_topic_name.val("");
              edit_topic_description.val("");
              edit_topic_content.val("");
            }


          }
        });

        var get_topic_status= {
          "topic_id": topicParams.get('id'),
          "get_topic_status": "yes"
        }

        $.get("UtilServlet", get_topic_status, function(data){
          if (data!=true) {
            $(".registered_add_option").show();
            var add_reply = $("#add_reply_registered");
            var add_reply_window = $(".add_reply_window");
            var add_reply_submit = $("#add_reply_submit");
            var add_reply_close = $("#add_reply_close");
            var add_reply_content = $("#add_reply_content");

            // var edit_reply_window = $(".edit_reply_window");
            // var edit_reply_close = $("#edit_reply_close");
            // var edit_reply_submit = $("#edit_reply_submit");
            // var edit_reply_content = $("#edit_reply_content");

            $(add_reply).click(function(e) {
              e.stopPropagation();
              add_reply_window.show();
            });

            $(add_reply_submit).click(function(e) {
              e.stopPropagation();
              var rc = add_reply_content.val();
              var owner = logged_user.text();
              var action = "create";
              var parentTopic = topicParams.get('id');

              if (rc === "") {
                p_message.text("* Marked fields must be filled")
                notification_window.show().delay(2000).fadeOut();
              } else {
                var jsonNewReply = {
                  'action': action,
                  'owner': owner,
                  'parentId': parentTopic,
                  'content': rc
                };
                $.post('ReplyServlet', jsonNewReply, function(data) {
                  p_message.text(data.message);
                  notification_window.show().delay(2000).fadeOut();
                  if (data.status === "success") {
                    window.location.replace("topic.html?id=" + topicParams.get("id"));
                  }
                });
              }
            });

            $(add_reply_close).click(function(e) {
              e.stopPropagation();
              resetAddReplyInput();
              add_reply_window.hide();
            });


            function resetAddReplyInput() {
              add_reply_content.val("");
            }

            var edit_reply_window = $(".edit_reply_window");
            var edit_reply_close = $("#edit_reply_close");
            var edit_reply_submit = $("#edit_reply_submit");
            var edit_reply_content = $("#edit_reply_content");

            topic_replies.on('click', 'button.edit_reply', function(e) {
              e.preventDefault();
              var reply_id = $(this).attr('reply_id');
              var row = $(this).closest('tr'); //najblizi njemu jer taj tr sadrzi bas taj input
              var rc_tbox = row.find("#reply_content"+reply_id);
              var text = rc_tbox.val();
              edit_reply_content.val(text);

              edit_reply_window.show();

              $(edit_reply_submit).click(function(e) {
                e.stopPropagation();
                var action = "edit";
                var rc = edit_reply_content.val();
                if (rc === "") {
                  p_message.text("* Marked fields must be filled")
                  notification_window.show().delay(2000).fadeOut();
                } else {
                  var jsonEditReply = {
                    'action': action,
                    'reply_id': reply_id,
                    'content': rc
                  };

                  $.post('ReplyServlet', jsonEditReply, function(data) {
                    if (data.status === "success") {
                      window.location.replace("topic.html?id=" + topicParams.get("id"));
                    } else {
                      p_message.text(data.message);
                      notification_window.show().delay(2000).fadeOut();
                    }
                  });
                }
              });
            });

            function resetEditReplyInput() {
              edit_reply_content.val("");
            }

            $(edit_reply_close).click(function(e) {
              e.stopPropagation();
              resetEditReplyInput();
              edit_reply_window.hide();
            });

            topic_replies.on('click', 'button.delete_reply', function(e) {
              e.preventDefault();
              if (confirm("Are you sure you want to delete this reply?")) {
                var reply_id = $(this).attr('reply_id');
                var row = $(this).closest('tr'); //najblizi njemu jer taj tr sadrzi bas taj input

                $.post('ReplyServlet', {
                  'action': 'delete',
                  'reply_id': reply_id
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


        $(reply_next).click(function(e) {
          e.stopPropagation();
          reply_page++;
          reply_page_html.text(reply_page);
          getReplies("registered");
        });

        $(reply_back).click(function(e) {
          e.stopPropagation();
          if (reply_page > 1) {
            reply_back.prop("disabled", false);
            reply_page--;
            reply_page_html.text(reply_page);
            // getForums("admin");
            getReplies("registered");
          }
        });

        $('.pagination').on('change', '#reply_results', function() {
          reply_results = parseInt(reply_results_html.find(":selected").text());
          reply_page = 1;
          reply_page_html.text(reply_page);
          // getForums("admin");
          getReplies("registered");
        });

      }

      function moderatorInterface() {

        var get_canEditDelete = {
          "topic_id": topicParams.get('id'),
          "get_canEditDelete": "yes"
        }
        $.get("UtilServlet", get_canEditDelete, function(data) {
          if (data == true) {
            var moderator_options = $(".moderator_options");
            moderator_options.show();
            var delete_topic = $("#delete_topic_moderator");
            var edit_topic = $("#edit_topic_moderator");
            var edit_topic_window = $(".edit_topic_window");
            var edit_topic_submit = $("#edit_topic_submit");
            var edit_topic_close = $("#edit_topic_close");
            var edit_topic_name = $("#edit_topic_name");
            var edit_topic_description = $("#edit_topic_description");
            var edit_topic_content = $("#edit_topic_content");

            $(edit_topic).click(function(e) {
              e.preventDefault();
              e.stopPropagation();
              edit_topic_window.show();
              edit_topic_name.val($("#topic_name").text());
              edit_topic_description.val($("#topic_description").text());
              edit_topic_content.val($("#topic_content").val());
            });

            $(edit_topic_submit).click(function(e) {
              e.stopPropagation();
              var tn = edit_topic_name.val();
              var td = edit_topic_description.val();
              var tc = edit_topic_content.val();
              var action = "edit";
              var topic_id = topicParams.get('id');

              if (tn === "" || tc === "") {
                p_message.text("* Marked fields must be filled")
                notification_window.show().delay(2000).fadeOut();
              } else {

                var jsonEditTopic = {
                  'action': action,
                  'topic_id': topic_id,
                  'name': tn,
                  'description': td,
                  'content': tc

                };

                $.post('TopicServlet', jsonEditTopic, function(data) {
                  p_message.text(data.message);
                  notification_window.show().delay(2000).fadeOut();
                  if (data.status === "success") {
                    window.location.replace("topic.html?id=" + topicParams.get("id"));
                  }
                });
              }
            });

            $(delete_topic).click(function(e) {
                e.preventDefault();
                if (confirm("Are you sure you want to delete this topic?")) {
                  e.stopPropagation();
                  var action = "delete";
                  var topic_id = topicParams.get('id');
                  var jsonDeleteTopic = {
                    'action': action,
                    'topic_id': topic_id
                  };

                  $.post('TopicServlet', jsonDeleteTopic, function(data) {
                    p_message.text(data.message);
                    notification_window.show().delay(2000).fadeOut();
                    if (data.status === "success") {
                      window.location.replace("forum.html?id=" + parent_forum_id);
                    }
                  });
                }
                });

              $(edit_topic_close).click(function(e) {
                e.stopPropagation();
                resetEditTopicInput();
                edit_topic_window.hide();
              });

              function resetEditTopicInput() {
                edit_topic_name.val("");
                edit_topic_description.val("");
                edit_topic_content.val("");
              }
            }
        });

        var get_user_status = {
          "username": logged_user.text(),
          "get_user_status": "yes"
        }
        $.get("UtilServlet", get_user_status, function(banned) {
          if (!banned) {

        $(".moderator_add_option").show();
        var add_reply = $("#add_reply_moderator");
        var add_reply_window = $(".add_reply_window");
        var add_reply_submit = $("#add_reply_submit");
        var add_reply_close = $("#add_reply_close");
        var add_reply_content = $("#add_reply_content");

        var edit_reply_window = $(".edit_reply_window");
        var edit_reply_close = $("#edit_reply_close");
        var edit_reply_submit = $("#edit_reply_submit");
        var edit_reply_content = $("#edit_reply_content");

        $(add_reply).click(function(e) {
          e.stopPropagation();
          add_reply_window.show();
        });

        $(add_reply_submit).click(function(e) {
          e.stopPropagation();
          var rc = add_reply_content.val();
          var owner = logged_user.text();
          var action = "create";
          var parentTopic = topicParams.get('id');

          if (rc === "") {
            p_message.text("* Marked fields must be filled")
            notification_window.show().delay(2000).fadeOut();
          } else {
            var jsonNewReply = {
              'action': action,
              'owner': owner,
              'parentId': parentTopic,
              'content': rc
            };
            $.post('ReplyServlet', jsonNewReply, function(data) {
              p_message.text(data.message);
              notification_window.show().delay(2000).fadeOut();
              if (data.status === "success") {
                window.location.replace("topic.html?id=" + topicParams.get("id"));
              }
            });
          }
        });

        $(add_reply_close).click(function(e) {
          e.stopPropagation();
          resetAddReplyInput();
          add_reply_window.hide();
        });


        function resetAddReplyInput() {
          add_reply_content.val("");
        }

        var edit_reply_window = $(".edit_reply_window");
        var edit_reply_close = $("#edit_reply_close");
        var edit_reply_submit = $("#edit_reply_submit");
        var edit_reply_content = $("#edit_reply_content");

        topic_replies.on('click', 'button.edit_reply', function(e) {
          e.preventDefault();
          var reply_id = $(this).attr('reply_id');
          var row = $(this).closest('tr'); //najblizi njemu jer taj tr sadrzi bas taj input
          var rc_tbox = row.find("#reply_content"+reply_id);
          var text = rc_tbox.val();
          edit_reply_content.val(text);

          edit_reply_window.show();

          $(edit_reply_submit).click(function(e) {
            e.stopPropagation();
            var action = "edit";
            var rc = edit_reply_content.val();
            if (rc === "") {
              p_message.text("* Marked fields must be filled")
              notification_window.show().delay(2000).fadeOut();
            } else {
              var jsonEditReply = {
                'action': action,
                'reply_id': reply_id,
                'content': rc
              };

              $.post('ReplyServlet', jsonEditReply, function(data) {
                if (data.status === "success") {
                  window.location.replace("topic.html?id=" + topicParams.get("id"));
                } else {
                  p_message.text(data.message);
                  notification_window.show().delay(2000).fadeOut();
                }
              });
            }
          });
        });

        function resetEditReplyInput() {
          edit_reply_content.val("");
        }

        $(edit_reply_close).click(function(e) {
          e.stopPropagation();
          resetEditReplyInput();
          edit_reply_window.hide();
        });

        topic_replies.on('click', 'button.delete_reply', function(e) {
          e.preventDefault();
          if (confirm("Are you sure you want to delete this reply?")) {
            var reply_id = $(this).attr('reply_id');
            var row = $(this).closest('tr'); //najblizi njemu jer taj tr sadrzi bas taj input

            $.post('ReplyServlet', {
              'action': 'delete',
              'reply_id': reply_id
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

      }});

        $(reply_next).click(function(e) {
          e.stopPropagation();
          reply_page++;
          reply_page_html.text(reply_page);
          getReplies("moderator");
        });

        $(reply_back).click(function(e) {
          e.stopPropagation();
          if (reply_page > 1) {
            reply_back.prop("disabled", false);
            reply_page--;
            reply_page_html.text(reply_page);
            // getForums("admin");
            getReplies("moderator");
          }
        });

        $('.pagination').on('change', '#reply_results', function() {
          reply_results = parseInt(reply_results_html.find(":selected").text());
          reply_page = 1;
          reply_page_html.text(reply_page);
          // getForums("admin");
          getReplies("moderator");
        });


      }



      function adminInterface() {

        var get_user_status = {
          "username": logged_user.text(),
          "get_user_status": "yes"
        }
        $.get("UtilServlet", get_user_status, function(banned) {
          if (!banned) {
        admin_options.show();
        var delete_topic = $("#delete_topic");
        var edit_topic = $("#edit_topic");
        var edit_topic_window = $(".edit_topic_window");
        var edit_topic_submit = $("#edit_topic_submit");
        var edit_topic_close = $("#edit_topic_close");
        var edit_topic_name = $("#edit_topic_name");
        var edit_topic_description = $("#edit_topic_description");
        var edit_topic_content = $("#edit_topic_content");

        $(edit_topic).click(function(e) {
          e.preventDefault();
          e.stopPropagation();
          edit_topic_window.show();
          edit_topic_name.val($("#topic_name").text());
          edit_topic_description.val($("#topic_description").text());
          edit_topic_content.val($("#topic_content").val());
        });

        $(edit_topic_submit).click(function(e) {
          e.stopPropagation();
          var tn = edit_topic_name.val();
          var td = edit_topic_description.val();
          var tc = edit_topic_content.val();
          var action = "edit";
          var topic_id = topicParams.get('id');

          if (tn === "" || tc === "") {
            p_message.text("* Marked fields must be filled")
            notification_window.show().delay(2000).fadeOut();
          } else {

            var jsonEditTopic = {
              'action': action,
              'topic_id': topic_id,
              'name': tn,
              'description': td,
              'content': tc

            };

            $.post('TopicServlet', jsonEditTopic, function(data) {
              p_message.text(data.message);
              notification_window.show().delay(2000).fadeOut();
              if (data.status === "success") {
                window.location.replace("topic.html?id=" + topicParams.get("id"));
              }
            });
          }
        });

        $(delete_topic).click(function(e) {
          e.preventDefault();
          if (confirm("Are you sure you want to delete this topic?")) {
            e.stopPropagation();
            var action = "delete";
            var topic_id = topicParams.get('id');
            var jsonDeleteTopic = {
              'action': action,
              'topic_id': topic_id
            };

            $.post('TopicServlet', jsonDeleteTopic, function(data) {
              p_message.text(data.message);
              notification_window.show().delay(2000).fadeOut();
              if (data.status === "success") {
                window.location.replace("forum.html?id=" + parent_forum_id);
              }
            });

          }
        });

        $(edit_topic_close).click(function(e) {
          e.stopPropagation();
          resetEditTopicInput();
          edit_topic_window.hide();
        });

        function resetEditTopicInput() {
          edit_topic_name.val("");
          edit_topic_description.val("");
          edit_topic_content.val("");
        }


        var add_reply = $("#add_reply");
        var add_reply_window = $(".add_reply_window");
        var add_reply_submit = $("#add_reply_submit");
        var add_reply_close = $("#add_reply_close");
        var add_reply_content = $("#add_reply_content");

        var edit_reply_window = $(".edit_reply_window");
        var edit_reply_close = $("#edit_reply_close");
        var edit_reply_submit = $("#edit_reply_submit");
        var edit_reply_content = $("#edit_reply_content");

        $(add_reply).click(function(e) {
          e.stopPropagation();
          add_reply_window.show();
        });

        $(add_reply_submit).click(function(e) {
          e.stopPropagation();
          var rc = add_reply_content.val();
          var owner = logged_user.text();
          var action = "create";
          var parentTopic = topicParams.get('id');

          if (rc === "") {
            p_message.text("* Marked fields must be filled")
            notification_window.show().delay(2000).fadeOut();
          } else {
            var jsonNewReply = {
              'action': action,
              'owner': owner,
              'parentId': parentTopic,
              'content': rc
            };
            $.post('ReplyServlet', jsonNewReply, function(data) {
              p_message.text(data.message);
              notification_window.show().delay(2000).fadeOut();
              if (data.status === "success") {
                window.location.replace("topic.html?id=" + topicParams.get("id"));
              }
            });
          }
        });

        $(add_reply_close).click(function(e) {
          e.stopPropagation();
          resetAddReplyInput();
          add_reply_window.hide();
        });


        function resetAddReplyInput() {
          add_reply_content.val("");
        }

        topic_replies.on('click', 'button.edit_reply', function(e) {
          e.preventDefault();
          var reply_id = $(this).attr('reply_id');
          var row = $(this).closest('tr'); //najblizi njemu jer taj tr sadrzi bas taj input
          var rc_tbox = row.find("#reply_content"+reply_id);
          var text = rc_tbox.val();
          edit_reply_content.val(text);

          edit_reply_window.show();

          $(edit_reply_submit).click(function(e) {
            e.stopPropagation();
            var action = "edit";
            var rc = edit_reply_content.val();
            if (rc === "") {
              p_message.text("* Marked fields must be filled")
              notification_window.show().delay(2000).fadeOut();
            } else {
              var jsonEditReply = {
                'action': action,
                'reply_id': reply_id,
                'content': rc
              };


              $.post('ReplyServlet', jsonEditReply, function(data) {
                if (data.status === "success") {
                  window.location.replace("topic.html?id=" + topicParams.get("id"));
                } else {
                  p_message.text(data.message);
                  notification_window.show().delay(2000).fadeOut();
                }
              });
            }
          });
        });

        function resetEditReplyInput() {
          edit_reply_content.val("");
        }

        $(edit_reply_close).click(function(e) {
          e.stopPropagation();
          resetEditReplyInput();
          edit_reply_window.hide();
        });

        topic_replies.on('click', 'button.delete_reply', function(e) {
          e.preventDefault();
          if (confirm("Are you sure you want to delete this reply?")) {
            var reply_id = $(this).attr('reply_id');
            var row = $(this).closest('tr'); //najblizi njemu jer taj tr sadrzi bas taj input

            $.post('ReplyServlet', {
              'action': 'delete',
              'reply_id': reply_id
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

      }});

        $(reply_next).click(function(e) {
          e.stopPropagation();
          reply_page++;
          reply_page_html.text(reply_page);
          getReplies("admin");
        });

        $(reply_back).click(function(e) {
          e.stopPropagation();
          if (reply_page > 1) {
            reply_back.prop("disabled", false);
            reply_page--;
            reply_page_html.text(reply_page);
            // getForums("admin");
            getReplies("admin");
          }
        });

        $('.pagination').on('change', '#reply_results', function() {
          reply_results = parseInt(reply_results_html.find(":selected").text());
          reply_page = 1;
          reply_page_html.text(reply_page);
          // getForums("admin");
          getReplies("admin");
        });


      } ///end of admin interface



      function appendTopicDetails(details) {
        //lista pa mora ovako
        $("#topic_content").val(details[2]);
        var topic_details = $("#topic_details");
        topic_details.find('tr:gt(0)').remove();
        topic_details.append(
          '<tr>' +
          '<td>Name</td>' +
          '<td id="topic_name">' + details[0] + '</td>' + //name
          '</tr>' +
          '<tr>' +
          '<td>Description</td>' +
          '<td id="topic_description">' + details[1] + '</td>' + //description
          '</tr>' +
          '<tr>' +
          '<td>Owner</td>' +
          '<td><img src="' + details[5] + '" alt="user_img"><a href="user.html?username=' + details[3] + '">' + //username
          details[3] +
          '</a></td>' +
          '</tr>' +
          '<tr>' +
          '<td>Date Created</td>' +
          '<td>' + details[4] + '</td>' + ///date
          '</tr>')
      }


      function appendReplyAdmin(reply) {
        topic_replies.append(
          '<tr><td>' +
          '<a href="reply.html?id=' + reply.id + '">Reply</a>' +
          '<p>' + reply.creationDate + '</p>' +
          '<a href="user.html?username=' + reply.ownerUsername + '">' +
          '<img src="'+reply.ownerPicture+'" alt="img">' + reply.ownerUsername + '</a>' +
          '<form style="display:inline"><button class="no_button edit_reply" type="submit" value="edit"  reply_id="' + reply.id + '"><span class="glyphicon glyphicon-edit" title="Edit This Reply"></span></button></form>' +
          '<form style="display:inline"><button class="no_button delete_reply" type="submit" value="delete" reply_id="' + reply.id + '"><span class="glyphicon glyphicon-remove" title="Remove This Reply"></span></button></form>' +
          '</td>' +
          '<td><textarea id="reply_content'+reply.id+'" name="" class="" cols="80" rows="5" readonly="true"></textarea></td></tr>'
        )
        topic_replies.find("#reply_content"+reply.id).text(reply.content);

      }


      function appendReplyGuest(reply) {
        topic_replies.append(
          '<tr><td>' +
          '<a href="reply.html?id=' + reply.id + '">Reply</a>' +
          '<p>' + reply.creationDate + '</p>' +
          '<a href="user.html?username=' + reply.ownerUsername + '">' +
          '<img src="'+reply.ownerPicture+'" alt="img">' + reply.ownerUsername + '</a>' +
          '</td>' +
          '<td><textarea id="reply_content'+reply.id+'" name="" class="" cols="80" rows="5" readonly="true"></textarea></td></tr>'
        )
        topic_replies.find("#reply_content"+reply.id).text(reply.content);

      }


      function getReplies(role) {
        if (role === "admin") {
          var req = {
            "topic_id": topicParams.get('id'),
            "reply_page": reply_page,
            "reply_results": reply_results
          }

          $.get("ReplyServlet", req, function(data) {
            topic_replies.find('tr:gt(0)').remove();
            for (var i = 0; i < data.replies.length; i++) {
              appendReplyAdmin(data.replies[i]);

            }
            appendTopicDetails(data.details);
          });
        }else
        if (role === "moderator") {
          var req = {
            "topic_id": topicParams.get('id'),
            "reply_page": reply_page,
            "reply_results": reply_results
          }

          $.get("ReplyServlet", req, function(data) {
            topic_replies.find('tr:gt(0)').remove();
            for (var i = 0; i < data.replies.length; i++) {
              if (data.canEditDelete.includes(data.replies[i].id)) {
                appendReplyAdmin(data.replies[i]);
              }else {
                appendReplyGuest(data.replies[i]);
              }
            }
            appendTopicDetails(data.details);
          });
        }
        else
        if (role === "registered") {
          var req = {
            "topic_id": topicParams.get('id'),
            "reply_page": reply_page,
            "reply_results": reply_results
          }

          $.get("ReplyServlet", req, function(data) {
            topic_replies.find('tr:gt(0)').remove();
            for (var i = 0; i < data.replies.length; i++) {
              if (data.canEditDelete.includes(data.replies[i].id) && data.topicStatus==false) {
                appendReplyAdmin(data.replies[i]);
              }else {
                appendReplyGuest(data.replies[i]);
              }
            }
            appendTopicDetails(data.details);
          });
        }else {
            var req = {
              "topic_id": topicParams.get('id'),
              "reply_page": reply_page,
              "reply_results": reply_results
            }

            $.get("ReplyServlet", req, function(data) {
              topic_replies.find('tr:gt(0)').remove();
              for (var i = 0; i < data.replies.length; i++) {
                appendReplyGuest(data.replies[i]);
              }
              appendTopicDetails(data.details);
            });
          }






      }



    });

  }
});
