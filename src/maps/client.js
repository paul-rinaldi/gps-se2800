class Client {
    static start() {
        new Client();
    }

    constructor() {
        $(document).ready(function() {
            onLoad();
        });

        // https://www.google.com/maps/embed/v1/MODE?key=YOUR_API_KEY&parameters

        let onLoad = function() {
            alert("TEST ALERT");

        };
    }
}