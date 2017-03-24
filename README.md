# Demonstration of Closure compiler not removing dead components

Run `boot build` and examine compiled file.

Expected result: one `SHOULD BE PRESENT`, none of `SHOULD NOT BE PRESENT`

Actual result: because components creation functions are not trimmed,
two `SHOULD NOT BE PRESENT` are present.

## If you want to run the code

`$ node target/run-server.js`

Visit http://localhost:3000

## Dependencies
- java (only for building)
- https://github.com/boot-clj/boot
- (optional) node for simple webserver
