$(document).ready(function() {

  var p_message = $("#message");
  var notification_window = $("#notification_window");

  var search_forum_button = $("#big_search");

  var forum_page_html = $("#forum_page");
  var forum_page = 1
  // parseInt(forum_page_html.text());

  var forum_results_html = $("#forum_results");
  var forum_results = parseInt(forum_results_html.find(":selected").text());
  var forum_back = $("#forum_back");
  var forum_next = $("#forum_next");

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
      } else if (data.role === "registered") {
        registeredInterface();
        getForums("registered");
      } else if (data.role === "moderator") {
        moderatorInterface();
        getForums("moderator");
      } ////else if neka druga uloga radi drugi posao

    } else {

      getForums("guest");

      ///pagination next for guest
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
        forum_results=parseInt(forum_results_html.find(":selected").text());
        forum_page=1;
        forum_page_html.text(forum_page);
        getForums("guest");
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
                // window.location.replace("index.html");
                resetPaganationHandlers();
                adminInterface();
                getForums(data.role);
              } else if (data.role === "registered") {
                // window.location.replace("index.html");
                resetPaganationHandlers();
                registeredInterface();
                //ver cu zvati regi interface da stavi handler na pagi
                getForums(data.role);
              } else if (data.role === "moderator") {
                resetPaganationHandlers();
                moderatorInterface();
                //var cu zvati interface njihov
                // window.location.replace("index.html");
                getForums(data.role);
              }

            }
          });
        }
      });



      function resetPaganationHandlers(){
        forum_next.off();
        forum_back.off();
        forum_page_html.text("1");
        forum_page=1;
        forum_results_html.val("5"); /// select option automatically
        forum_results=5;
      }

    }///nije ulogovan kraj

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

      var add_forum_name = $("#add_forum_name");
      var add_forum_description = $("#add_forum_description");

      var edit_forum_window = $(".edit_forum_window");
      var edit_forum_submit = $("#edit_forum_submit");
      var edit_forum_close = $("#edit_forum_close");
      var edit_forum_name = $("#edit_forum_name");
      var edit_forum_description = $("#edit_forum_description");

      $(add_forum).click(function(e) {
        e.stopPropagation();
        add_forum_window.show();
      });

      $(add_forum_submit).click(function(e) {
        e.stopPropagation();
        var fn = add_forum_name.val();
        var fd = add_forum_description.val();
        var type = $('#add_forum_type').find(":selected").text();
        var owner = logged_user.text();
        var action = "create";
        var parentForum = 0;

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
              window.location.replace("index.html");
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
        // var f_t = row.find(":eq(17)").text();

        edit_forum_window.show();
        edit_forum_name.val(f_n);
        edit_forum_description.val(f_d);


        $(edit_forum_submit).click(function(e) {
          e.stopPropagation();
          var fn = edit_forum_name.val();
          var fd = edit_forum_description.val();
          var type = $('#edit_forum_type').find(":selected").text();
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
                window.location.replace("index.html");
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

    }});

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
        forum_results=parseInt(forum_results_html.find(":selected").text());
        forum_page=1;
        forum_page_html.text(forum_page);
        getForums("admin");
      });


    } ///end of admin interface



    function moderatorInterface() {

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
        forum_results=parseInt(forum_results_html.find(":selected").text());
        forum_page=1;
        forum_page_html.text(forum_page);
        getForums("guest");
      });

    }

    function registeredInterface() {

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
        forum_results=parseInt(forum_results_html.find(":selected").text());
        forum_page=1;
        forum_page_html.text(forum_page);
        getForums("guest");
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
          "forum_page": forum_page,
          "forum_results": forum_results
        }

        $.get("ForumServlet", req, function(data) {
          page_forums.find('tr:gt(0)').remove();
          for (var i = 0; i < data.length; i++) {
            appendForumAdmin(data[i]);
          }

        });

      } else if (role === "moderator") {
        var req = {
          "forum_page": forum_page,
          "forum_results": forum_results
        }

        $.get("ForumServlet", req, function(data) {
          page_forums.find('tr:gt(0)').remove();
          for (var i = 0; i < data.length; i++) {
            appendForumGuest(data[i]);
          }

        });

      } else if (role === "registered") {
        var req = {
          "forum_page": forum_page,
          "forum_results": forum_results
        }

        $.get("ForumServlet", req, function(data) {
          page_forums.find('tr:gt(0)').remove();
          for (var i = 0; i < data.length; i++) {
            appendForumGuest(data[i]);
          }

        });

      } else {
        var req = {
          "forum_page": forum_page,
          "forum_results": forum_results
        }

        $.get("ForumServlet", req, function(data) {
          page_forums.find('tr:gt(0)').remove();
          for (var i = 0; i < data.length; i++) {
            appendForumGuest(data[i]);
          }

        });
      }

    }




  });
});
