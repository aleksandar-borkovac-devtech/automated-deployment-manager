Ext.onReady(function() {

	Ext.QuickTips.init();

	// turn on validation errors beside the field globally
		Ext.form.Field.prototype.msgTarget = 'side';

		var bd = Ext.getBody();

		var simple = new Ext.FormPanel( {
			labelWidth : 75, // label settings here cascade unless overridden
			url : 'j_spring_security_check',
			method : 'POST',
			standardSubmit : true,
			frame : true,
			title : 'Login Form',
			bodyStyle : 'padding:5px 5px 0',
			width : 350,
			monitorValid: true,
			floating: true,
			shadow: true,
			defaults : {
				width : 230
			},
			defaultType : 'textfield',

			items : [ {
				fieldLabel : 'Username',
				name : 'j_username',
				allowBlank : false
			}, {
				fieldLabel : 'Password',
				name : 'j_password',
				inputType : 'password',
				allowBlank : false,
                listeners:{
                    scope:this,
                    specialkey: function(f,e){
                        if(e.getKey()==e.ENTER){
                        	Ext.getCmp('login-btn').focus();
                        }
                    }
                }				
			} ],

			buttons : [ {
				id : 'login-btn',
				name : 'login-btn',
				text : 'Login',
				formBind: true,
				handler : function() {
					var fp = this.ownerCt.ownerCt, form = fp.getForm();

					form.submit();
				}
			} ]
		});

		simple.render(document.body);
		simple.getEl().center();
		simple.show();
	});