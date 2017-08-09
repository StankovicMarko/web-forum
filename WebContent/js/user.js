$(document).ready(function() {

  let userParams = new URLSearchParams(window.location.search);
  if (!userParams.has('username')) {
    window.location.replace("index.html");
  } else {

    var search_forum_button = $("#big_search");
    var user_topics = $("#user_topics");
    var user_replies = $("#user_replies");
    var all_users_details = $("#all_users_details");

    var p_message = $("#message");
    var notification_window = $("#notification_window");

    var logged_in = $("#logged_in");
    var logged_out = $("#logged_out");
    var logged_user = $("#logged_user");

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
        if (data.username === userParams.get("username") && data.role === "admin") {
          showEditOptions();
          adminInterface();
          getUserDetailsAndPannel(); //get all users, topics, replies
        } else if (data.username === userParams.get("username")) {
          showEditOptions();
          getUserDetailsWithOptions();
        } else {
          getUserDetails();

        } ////else if neka druga uloga radi drugi posao

      } else {

        getUserDetails();

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
                if (data.username === userParams.get("username") && data.role === "admin") {
                  showEditOptions();
                  adminInterface();
                  getUserDetailsAndPannel();
                } else if (data.username === userParams.get("username")) {
                  showEditOptions();
                  getUserDetailsWithOptions();
                } else {
                  getUserDetails();

                }

              }
            });
          }
        });


      } ///nije ulogovan kraj




      function showEditOptions() {
        $(".user_password").show();
        $(".user_avatar").show();
        $(".user_details").show();
        $(".topics").show();
        $(".replies").show();

        var change_details = $("#change_details");
        var change_pass = $("#change_pass");

        $(change_details).click(function(e) {
          e.stopPropagation();
          e.preventDefault();

          var new_name = $("#new_name").val();
          var new_surName = $("#new_surName").val();

          if (new_name === "" || new_surName === "") {
            p_message.text("* Marked fields must be filled")
            notification_window.show().delay(2000).fadeOut();
          } else {
            var jsonNameSurname = {
              'action': 'updateName',
              'username': userParams.get("username"),
              'new_name': new_name,
              'new_surName': new_surName
            };
            $.post('UserServlet', jsonNameSurname, function(data) {
              p_message.text(data.message);
              notification_window.show().delay(2000).fadeOut();
              if (data.status === "success") {
                window.location.replace("user.html?username=" + userParams.get("username"));
              }
            });
          }
        });

        $(change_pass).click(function(e) {
          e.stopPropagation();
          e.preventDefault();

          var old_pass = $("#old_pass").val();
          var new_pass = $("#new_pass").val();

          if (old_pass === "" || new_pass === "") {
            p_message.text("* Marked fields must be filled")
            notification_window.show().delay(2000).fadeOut();
          } else {
            var jsonNewPass = {
              'action': 'updatePass',
              'username': userParams.get("username"),
              'old_pass': old_pass,
              'new_pass': new_pass
            };
            $.post('UserServlet', jsonNewPass, function(data) {
              p_message.text(data.message);
              notification_window.show().delay(2000).fadeOut();
              if (data.status === "success") {
                window.location.replace("user.html?username=" + userParams.get("username"));
              }
            });
          }
        });


        $("#usn").attr("value", userParams.get("username"));


      }

      function appendDetails(user) {
        $("#user_picture").attr("src", user.picture);
        $("#username").val(user.username);
        $("#mail").val(user.mail);
        $("#dateReg").val(user.dateReg);
        $("#role").val(user.role);
        $("#status").val(user.banned);

        if (user.username === $("#logged_user").text()) {
          $("#name").val(user.name);
          $("#surName").val(user.surName);
        }

      }


      function appendUserTopic(topic) {
        user_topics.append(
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

      function appendUserReply(reply) {
        user_replies.append(
          '<tr><td>' +
          '<a href="topic.html?id=' + reply.parentTopicId + '">' +
          'Parent Topic' +
          '</a>' +
          '<p>' + reply.creationDate + '</p>' +
          '<a href="user.html?username=' + reply.ownerUsername + '">' +
          '<img src="images/default-avatar.png" alt="img">' + reply.ownerUsername + '</a>' +
          '</td>' +
          '<td><textarea id="reply_content' + reply.id + '" name="" class="" cols="80" rows="5" readonly="true"></textarea></td></tr>'
        )
        user_replies.find("#reply_content" + reply.id).text(reply.content);
      }


      function userStatus(bool) {
        if (!bool) {
          return '<span class="glyphicon glyphicon-ok" style="display: inline;" title="Not Banned"></span>';
        } else {
          return '<span class="glyphicon glyphicon-ban-circle" style="display: inline;" title="Banned"></span>';
        }
      }

      function appendUser(user) {
        all_users_details.append(
          '<tr>' +
          '<td> <img src="' + user.picture + '" alt="pi" style="height:60px; width:60px;"> </td>' +
          '  <td class="usn"> ' + user.username + ' </td>' +
          '  <td> ' + user.name + '  </td>' +
          '<td> ' + user.surName + '  </td>' +
          '<td class="mail"> ' + user.mail + ' </td>' +
          '  <td> ' + user.dateReg + ' </td>' +
          '  <td class="role"> ' + user.role + ' </td>' +
          '<td class="status"> ' + userStatus(user.banned) +
          ' </td>' +
          '<td>' +
          '<form style="display:inline">' +
          '<button class="no_button ban_user" type="submit" value="ban"' +
          'user_status="' + user.banned.toString() + '"' +
          'user_id="' + user.id + '">' +
          '<span class="glyphicon glyphicon-ban-circle"' +
          'title="Ban/Unban This User"></span> </button>' +
          '		</form>' +
          '<form style="display:inline">' +
          '<button class="no_button edit_user" type="submit" value="edit"' +
          'user_id="' + user.id + '">' +
          '<span class="glyphicon glyphicon-edit"' +
          'title="Edit This User"></span> </button>' +
          '		</form>' +
          '<form style="display:inline">' +
          '<button class="no_button delete_user" type="submit" value="delete"' +
          'user_id="' + user.id + '">' +
          '<span class="glyphicon glyphicon-remove"' +
          'title="Remove This User"></span> </button>' +
          '		</form>' +
          '  </td>' +
          '</tr>'
        );
      }


      function adminInterface() {

        var get_user_status = {
          "username": logged_user.text(),
          "get_user_status": "yes"
        }
        $.get("UtilServlet", get_user_status, function(banned) {
          if (!banned) {

        $(".all_users").show();
        var add_user = $("#add_new_user");
        var add_user_window = $(".add_user_window")
        var add_user_submit = $("#add_user_submit");
        var add_user_close = $("#add_user_close");
        var add_user_username = $("#add_user_username");
        var add_user_password = $("#add_user_password");
        var add_user_mail = $("#add_user_mail");
        var add_user_role = $("#add_user_role");

        $(add_user).click(function(e) {
          e.stopPropagation();
          add_user_window.show();
        });

        $(add_user_submit).click(function(e) {
          e.preventDefault();
          e.stopPropagation();
          var usn = add_user_username.val();
          var psw = add_user_password.val();
          var mail = add_user_mail.val();
          var role = add_user_role.find(":selected").val();
          var action = "create";

          if (usn === "" || psw === "" || mail === "") {
            p_message.text("* Marked fields must be filled")
            notification_window.show().delay(2000).fadeOut();
          } else {
            var jsonNewUser = {
              'action': action,
              'username': usn,
              'password': psw,
              'mail': mail,
              'role': role
            };
            $.post('UserServlet', jsonNewUser, function(data) {
              p_message.text(data.message);
              notification_window.show().delay(2000).fadeOut();
              if (data.status === "success") {
                window.location.replace("user.html?username=" + userParams.get("username"));
              }
            });
          }
        });

        $(add_user_close).click(function(e) {
          e.stopPropagation();
          resetAddUserInput();
          add_user_window.hide();
        });


        all_users_details.on('click', 'button.ban_user', function(e) {
          e.preventDefault();
          if (confirm("Are you sure you want to ban/unban this user?")) {
            var ban_btn = $(this);
            var user_id = ban_btn.attr('user_id');
            var status = ban_btn.attr('user_status');
            var row = ban_btn.closest('tr'); //najblizi njemu jer taj tr sadrzi bas taj input
            var status_td = row.find("td.status");

            var req;
            if (status == "true") {
              req = {
                'action': 'unban',
                'user_id': user_id
              }

            } else {
              req = {
                'action': 'ban',
                'user_id': user_id
              }

            }

            $.post('UserServlet', req, function(data) {
              if (data.message) {
                p_message.text(data.message)
                notification_window.show().delay(2000).fadeOut();
              }
              if (data.status === 'success') {
                if (status == "true") {
                  status_td.html(userStatus(false));
                  ban_btn.attr("user_status", "false");
                } else {
                  status_td.html(userStatus(true));
                  ban_btn.attr("user_status", "true");
                }


              }

            });

            // return false;

          }
        });


        all_users_details.on('click', 'button.delete_user', function(e) {
          e.preventDefault();
          if (confirm("Are you sure you want to delete this user?")) {
            var user_id = $(this).attr('user_id');
            var row = $(this).closest('tr'); //najblizi njemu jer taj tr sadrzi bas taj input

            $.post('UserServlet', {
              'action': 'delete',
              'user_id': user_id
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


        var edit_user = $("#edit_new_user");
        var edit_user_window = $(".edit_user_window")
        var edit_user_submit = $("#edit_user_submit");
        var edit_user_close = $("#edit_user_close");
        var edit_user_username = $("#edit_user_username");
        var edit_user_password = $("#edit_user_password");
        var edit_user_mail = $("#edit_user_mail");
        var edit_user_role = $("#edit_user_role");

        all_users_details.on('click', 'button.edit_user', function(e) {
          e.preventDefault();
          var user_id = $(this).attr('user_id');
          var row = $(this).closest('tr'); //najblizi njemu jer taj tr sadrzi bas taj input
          // console.log(row.find(".usn").text());
          edit_user_username.val(row.find(".usn").text());
          edit_user_mail.val(row.find(".mail").text());

          edit_user_window.show();

          $(edit_user_submit).click(function(e) {
            e.preventDefault();
            e.stopPropagation();
            var action = "edit";
            var password = edit_user_password.val();
            var mail = edit_user_mail.val();
            var role = edit_user_role.find(":selected").val();

            if (mail === "") {
              p_message.text("* Marked fields must be filled")
              notification_window.show().delay(2000).fadeOut();
            } else {

              var jsonEditUser = {
                'action': action,
                'user_id': user_id,
                'password': password,
                'mail': mail,
                'role': role
              }


              $.post('UserServlet', jsonEditUser, function(data) {
                if (data.status === "success") {
                  window.location.replace("user.html?username=" + userParams.get("username"));
                } else {
                  p_message.text(data.message);
                  notification_window.show().delay(2000).fadeOut();
                }
              });
            }
          });
        });

        // function resetEditUserInput() {
        //   edit_user_username.val("");
        //   edit_user_password.val("");
        //   edit_user_mail.val("");
        // }

        $(edit_user_close).click(function(e) {
          e.stopPropagation();
          // resetEditUserInput();
          edit_user_window.hide();
        });



        function resetAddUserInput() {
          add_user_username.val("");
          add_user_password.val("");
          add_user_mail.val("");

        }

      }});


      }


      function getUserDetails() {
        var req = {
          "username": userParams.get('username')
        }
        $.get("UserServlet", req, function(data) {
          appendDetails(data.user);
        });

      }

      function getUserDetailsWithOptions(){
        var req = {
          "username": userParams.get('username')
        }
        $.get("UserServlet", req, function(data) {
          appendDetails(data.user);

          user_topics.find('tr:gt(0)').remove();
          for (var i = 0; i < data.topics.length; i++) {
            appendUserTopic(data.topics[i]);
          }

          user_replies.find('tr:gt(0)').remove();
          for (var i = 0; i < data.replies.length; i++) {
            appendUserReply(data.replies[i]);
          }

        });


      }

      function getUserDetailsAndPannel() {
        var req = {
          "username": userParams.get('username')
        }
        $.get("UserServlet", req, function(data) {
          appendDetails(data.user);

          user_topics.find('tr:gt(0)').remove();
          for (var i = 0; i < data.topics.length; i++) {
            appendUserTopic(data.topics[i]);
          }

          user_replies.find('tr:gt(0)').remove();
          for (var i = 0; i < data.replies.length; i++) {
            appendUserReply(data.replies[i]);
          }

          if (data.hasOwnProperty("users")) {
            all_users_details.find('tr:gt(0)').remove();
            for (var i = 0; i < data.users.length; i++) {
              appendUser(data.users[i]);
            }
          }

        });


      }



    });

  }
});
