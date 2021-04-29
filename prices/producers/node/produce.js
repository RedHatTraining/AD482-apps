const fs = require("fs");
const { Kafka } = require("kafkajs");
require('dotenv').config()


const { KAFKA_BOOTSTRAP_SERVER, KAFKA_SSL_CAFILE } = process.env;

const kafka = new Kafka({
    clientId: "nodejs-price-producer",
    brokers: [KAFKA_BOOTSTRAP_SERVER],
    ssl: {
        ca: [fs.readFileSync(KAFKA_SSL_CAFILE, "utf-8")],
    }
});


produce();


async function produce() {
    const producer = kafka.producer();

    await producer.connect();

    send();

    async function send() {
        // Send an int value as bytes (32bit Big Endian integer)
        const price = getRandom(1000, 2000);
        const value = intToBytes(price);

        await producer.send({
            topic: 'prices',
            messages: [{ value }]
        });

        console.log("Sent", price);

        setTimeout(send, 2000);
    }
}


function intToBytes(price) {
    const value = Buffer.allocUnsafe(4);
    value.writeInt32BE(price);
    return value;
}

function getRandom(min, max) {
    return Math.floor(Math.random() * (max - min) + min);
}

