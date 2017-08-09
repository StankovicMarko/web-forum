$(document).ready(function() {

  let replyParams = new URLSearchParams(window.location.search);
  if (!replyParams.has('id')) {
    window.location.replace("index.html");
  } else {

    var parent_topic_id;
    var parent_topic = $("#parent_topic");
    var get_parent_id_req = {
      "reply_id": replyParams.get('id'),
      "get_parent_topic": "yes"
    }
    $.get("UtilServlet", get_parent_id_req, function(data) {
      if (data != 0) {
        parent_topic_id = data;
        parent_topic.append('<a href="topic.html?id=' + data + '">Back (to parent)</a>');
      }
    });
    var search_forum_button = $("#big_search");
    var topic_replies = $("#topic_replies");

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
        if (data.role === "admin") {
          getReply();
        } else if (data.role === "registered") {
          getReply();
        } else if (data.role === "moderator") {
          getReply();

        } ////else if neka druga uloga radi drugi posao

      } else {

        getReply();

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
                  getReply();
                } else if (data.role === "registered") {
                  getReply();
                } else if (data.role === "moderator") {
                  getReply();

                }

              }
            });
          }
        });


      } ///nije ulogovan kraj


      function appendReplyGuest(reply) {
        topic_replies.append(
          '<tr><td>' +
          '<p>' + reply.creationDate + '</p>' +
          '<a href="user.html?username=' + reply.ownerUsername + '">' +
          '<img src="images/default-avatar.png" alt="img">' + reply.ownerUsername + '</a>' +
          '</td>' +
          '<td><textarea id="reply_content'+reply.id+'" name="" class="" cols="80" rows="5" readonly="true"></textarea></td></tr>'
        )
        topic_replies.find("#reply_content"+reply.id).text(reply.content);

      }


      function getReply() {
        var req = {
          "reply_id": replyParams.get('id'),
          "reply_page": 1,
          "reply_results": 5
        }
          $.get("ReplyServlet",req, function(data) {
            topic_replies.find('tr:gt(0)').remove();
              appendReplyGuest(data);
          });

      }



    });

  }
});
