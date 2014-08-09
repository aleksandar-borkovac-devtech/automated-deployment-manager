Ext.onReady(function() {

	Ext.MessageBox.show( {
		title : 'Login failure',
		msg : 'Access denied! You have insufficient privileges to access this application.',
		buttons : Ext.MessageBox.OK,
		fn : function(btn, text) {
			if (btn == 'ok') {
				window.location = "login.html";
			}
		},
		icon : Ext.MessageBox.WARNING
	});

});