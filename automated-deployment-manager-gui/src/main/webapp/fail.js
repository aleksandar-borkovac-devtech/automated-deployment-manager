Ext.onReady(function() {

	Ext.MessageBox.show( {
		title : 'Login failure',
		msg : 'Invalid login credentials! Please try again.',
		buttons : Ext.MessageBox.OK,
		fn : function(btn, text) {
			if (btn == 'ok') {
				window.location = "login.html";
			}
		},
		icon : Ext.MessageBox.WARNING
	});

});