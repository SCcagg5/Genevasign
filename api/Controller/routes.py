from .routesfunc import *

def setuproute(app, call):
    @app.route('/test/',            ['OPTIONS', 'POST', 'GET'], lambda x = None: call([])                                   )
    @app.route('/login/',    	    ['OPTIONS', 'POST'],        lambda x = None: call([getauth])                            )
    @app.route('/infos/',    	    ['OPTIONS', 'POST'],        lambda x = None: call([myauth, signdoc])                        )
    def base():
        return
