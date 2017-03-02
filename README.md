# clj-stamp-duty

Example REST project in clojure.

## Prerequisites

You will need [Leiningen][] 2.0.0 or above installed.

[leiningen]: https://github.com/technomancy/leiningen

## Code Structure

`src/clj_stamp_duty`

Contains all of the source for the project. There are three files:
	`handler.clj` - contains the routes - well, the route really - there is only one. This is the
entry point of the application.
	`middleware.clj` - configuration related (how requests are handled - typically will contain
handlers for payload marshalling / unmarshalling, security features, caching - anything related to
how requests to the server are handled before they hit application code.
	`validation.clj` - because there wasn't much code in the app, i implemented a validation
framework!

## Running

To start a web server for the application, run:

    lein ring server-headless

Note - it may take awhile to start - downloading dependencies and so forth.

You should see something like below once it's ready to roll:

	socrates:clj-s-d simonbronner$ lein ring server-headless
	Started nREPL server on port 65104
	2017-03-02 15:54:03.531:INFO:oejs.Server:jetty-7.6.13.v20130916
	2017-03-02 15:54:03.586:INFO:oejs.AbstractConnector:Started SelectChannelConnector@0.0.0.0:3000
	Started server on port 3000

Access the following url for a test webpage: http://localhost:3000/index.html
