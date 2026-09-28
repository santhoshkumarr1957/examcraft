// script.js

const API = "http://localhost:8080";

let currentTestPaper = null;


// ===============================
// SECTION NAVIGATION
// ===============================

function showSection(section) {

    const sections = [
        "dashboard",
        "questions",
        "generate",
        "tests",
        "attemptTest",
        "attempts"
    ];

    sections.forEach(id => {
        document.getElementById(id).classList.add("hidden");
    });

    document.getElementById(section).classList.remove("hidden");

    window.scrollTo(0, 0);


    if (section === "questions") {
        loadQuestions();
    }

    if (section === "tests") {
        loadTestPapers();
    }

    if (section === "attempts") {
        loadAttempts();
    }
}


// ===============================
// ADD QUESTION
// ===============================

document.getElementById("questionForm").addEventListener(
    "submit",
    async function (event) {

        event.preventDefault();

        const question = {

            questionText:
                document.getElementById("questionText").value,

            topic:
                document.getElementById("topic").value,

            difficulty:
                document.getElementById("difficulty").value,

            questionType:
                document.getElementById("questionType").value,

            optionA:
                document.getElementById("optionA").value,

            optionB:
                document.getElementById("optionB").value,

            optionC:
                document.getElementById("optionC").value,

            optionD:
                document.getElementById("optionD").value,

            correctAnswer:
                document.getElementById("correctAnswer").value,

            unit: {
                id: Number(
                    document.getElementById("unitId").value
                )
            }
        };


        try {

            const response = await fetch(
                `${API}/questions/add`,
                {
                    method: "POST",

                    headers: {
                        "Content-Type": "application/json"
                    },

                    body: JSON.stringify(question)
                }
            );


            if (!response.ok) {
                throw new Error("Failed to add question");
            }


            document.getElementById("questionMessage").innerHTML =
                "✅ Question added successfully.";

            document.getElementById("questionForm").reset();

            document.getElementById("unitId").value = 1;

            loadQuestions();


        } catch (error) {

            document.getElementById("questionMessage").innerHTML =
                "❌ Failed to add question.";

            console.error(error);
        }

    }
);


// ===============================
// LOAD QUESTIONS
// ===============================

async function loadQuestions() {

    const list = document.getElementById("questionsList");

    list.innerHTML = "Loading questions...";


    try {

        const response =
            await fetch(`${API}/questions/get`);

        if (!response.ok) {
            throw new Error();
        }

        const questions = await response.json();


        if (questions.length === 0) {

            list.innerHTML =
                "<p>No questions available.</p>";

            return;
        }


        list.innerHTML = questions.map(
            (q, index) => `

            <div class="question-item">

                <h3>
                    ${index + 1}. ${escapeHtml(q.questionText)}
                </h3>

                <div class="question-options">

                    A. ${escapeHtml(q.optionA)}
                    &nbsp;&nbsp;

                    B. ${escapeHtml(q.optionB)}
                    &nbsp;&nbsp;

                    C. ${escapeHtml(q.optionC)}
                    &nbsp;&nbsp;

                    D. ${escapeHtml(q.optionD)}

                </div>

                <div class="question-meta">

                    Topic: ${escapeHtml(q.topic)}
                    |
                    Difficulty: ${q.difficulty}
                    |
                    Correct Answer: ${q.correctAnswer}

                </div>

            </div>

        `
        ).join("");


    } catch (error) {

        list.innerHTML =
            "<p>❌ Failed to load questions.</p>";

        console.error(error);
    }
}


// ===============================
// GENERATE TEST PAPER
// ===============================

document.getElementById("testForm").addEventListener(
    "submit",
    async function (event) {

        event.preventDefault();


        const title =
            document.getElementById("testTitle").value;

        const totalQuestions =
            Number(
                document.getElementById("totalQuestions").value
            );

        const easy =
            Number(
                document.getElementById("easyPercentage").value
            );

        const medium =
            Number(
                document.getElementById("mediumPercentage").value
            );

        const hard =
            Number(
                document.getElementById("hardPercentage").value
            );


        if (easy + medium + hard !== 100) {

            document.getElementById("testMessage").innerHTML =
                "❌ Easy + Medium + Hard percentages must equal 100%.";

            return;
        }


        try {

            const url =
                `${API}/testpapers/generate` +
                `?title=${encodeURIComponent(title)}` +
                `&totalQuestions=${totalQuestions}` +
                `&easyPercentage=${easy}` +
                `&mediumPercentage=${medium}` +
                `&hardPercentage=${hard}`;


            const response = await fetch(
                url,
                {
                    method: "POST"
                }
            );


            if (!response.ok) {

                const errorText =
                    await response.text();

                throw new Error(errorText);
            }


            const result =
                await response.json();


            document.getElementById("testMessage").innerHTML =
                `✅ Test paper "${escapeHtml(result.title)}" generated successfully.`;

            document.getElementById("testForm").reset();

            document.getElementById("easyPercentage").value = 40;
            document.getElementById("mediumPercentage").value = 40;
            document.getElementById("hardPercentage").value = 20;


        } catch (error) {

            document.getElementById("testMessage").innerHTML =
                "❌ Failed to generate test paper. " +
                error.message;

            console.error(error);
        }

    }
);


// ===============================
// LOAD TEST PAPERS
// ===============================

async function loadTestPapers() {

    const list =
        document.getElementById("testPapersList");

    list.innerHTML = "Loading test papers...";


    try {

        const response =
            await fetch(`${API}/testpapers/get`);


        if (!response.ok) {
            throw new Error();
        }


        const tests =
            await response.json();


        if (tests.length === 0) {

            list.innerHTML =
                "<p>No test papers available.</p>";

            return;
        }


        list.innerHTML = tests.map(
            test => `

            <div class="test-paper">

                <div class="test-paper-header">

                    <div>

                        <h2>
                            ${escapeHtml(test.title)}
                        </h2>

                        <div class="test-stats">

                            <span class="stat">
                                Total: ${test.totalQuestions}
                            </span>

                            <span class="stat">
                                Easy: ${test.easyCount}
                            </span>

                            <span class="stat">
                                Medium: ${test.mediumCount}
                            </span>

                            <span class="stat">
                                Hard: ${test.hardCount}
                            </span>

                        </div>

                    </div>


                    <div class="test-actions">

                        <button
                            class="attempt-btn"
                            onclick="startTest(${test.id})">

                            Attempt Test

                        </button>

                    </div>

                </div>

            </div>

        `
        ).join("");


    } catch (error) {

        list.innerHTML =
            "<p>❌ Failed to load test papers.</p>";

        console.error(error);
    }
}


// ===============================
// START TEST
// ===============================

async function startTest(id) {

    try {

        const response =
            await fetch(
                `${API}/testpapers/get/${id}`
            );


        if (!response.ok) {
            throw new Error();
        }


        currentTestPaper =
            await response.json();


        document.getElementById("attemptTitle").innerText =
            currentTestPaper.title;


        const container =
            document.getElementById("attemptQuestions");


        container.innerHTML =
            currentTestPaper.questions.map(
                (q, index) => `

                <div class="attempt-question">

                    <h3>
                        ${index + 1}.
                        ${escapeHtml(q.questionText)}
                    </h3>


                    <label class="option">

                        <input
                            type="radio"
                            name="question-${q.id}"
                            value="A">

                        A. ${escapeHtml(q.optionA)}

                    </label>


                    <label class="option">

                        <input
                            type="radio"
                            name="question-${q.id}"
                            value="B">

                        B. ${escapeHtml(q.optionB)}

                    </label>


                    <label class="option">

                        <input
                            type="radio"
                            name="question-${q.id}"
                            value="C">

                        C. ${escapeHtml(q.optionC)}

                    </label>


                    <label class="option">

                        <input
                            type="radio"
                            name="question-${q.id}"
                            value="D">

                        D. ${escapeHtml(q.optionD)}

                    </label>

                </div>

            `
            ).join("");


        document.getElementById("attemptResult").innerHTML = "";

        showSection("attemptTest");


    } catch (error) {

        alert(
            "Failed to load test paper."
        );

        console.error(error);
    }
}


// ===============================
// SUBMIT TEST
// ===============================

async function submitTest() {

    if (!currentTestPaper) {
        return;
    }


    const studentName =
        document.getElementById("studentName").value.trim();


    if (!studentName) {

        alert(
            "Please enter your name."
        );

        return;
    }


    const answers = [];


    for (const question of currentTestPaper.questions) {

        const selected =
            document.querySelector(
                `input[name="question-${question.id}"]:checked`
            );


        if (!selected) {

            alert(
                `Please answer question ${question.id}.`
            );

            return;
        }


        answers.push({

            questionId: question.id,

            selectedAnswer: selected.value

        });

    }


    try {

        const url =
            `${API}/attempts/submit` +
            `?testPaperId=${currentTestPaper.id}` +
            `&studentName=${encodeURIComponent(studentName)}`;


        const response =
            await fetch(
                url,
                {

                    method: "POST",

                    headers: {
                        "Content-Type": "application/json"
                    },

                    body: JSON.stringify(answers)

                }
            );


        if (!response.ok) {

            const error =
                await response.text();

            throw new Error(error);
        }


        const result =
            await response.json();


        document.getElementById("attemptResult").innerHTML = `

            <div class="result-box">

                Test submitted successfully!<br><br>

                Student:
                <strong>
                    ${escapeHtml(result.studentName)}
                </strong>

                <br>

                Score:
                <strong>
                    ${result.score} / ${result.totalMarks}
                </strong>

            </div>

        `;


    } catch (error) {

        alert(
            "Failed to submit test."
        );

        console.error(error);
    }
}


// ===============================
// LOAD ATTEMPTS
// ===============================

async function loadAttempts() {

    const list =
        document.getElementById("attemptsList");

    list.innerHTML =
        "Loading attempts...";


    try {

        const response =
            await fetch(`${API}/attempts/get`);


        if (!response.ok) {
            throw new Error();
        }


        const attempts =
            await response.json();


        if (attempts.length === 0) {

            list.innerHTML =
                "<p>No attempts found.</p>";

            return;
        }


        list.innerHTML =
            attempts.map(
                attempt => `

                <div class="attempt-row">

                    <div>

                        <strong>
                            ${escapeHtml(attempt.studentName)}
                        </strong>

                        <p>
                            Test:
                            ${escapeHtml(
                    attempt.testPaper.title
                )}
                        </p>

                        <p>
                            Submitted:
                            ${formatDate(
                    attempt.submittedAt
                )}
                        </p>

                    </div>


                    <div class="score">

                        ${attempt.score}
                        /
                        ${attempt.totalMarks}

                    </div>

                </div>

            `
            ).join("");


    } catch (error) {

        list.innerHTML =
            "<p>❌ Failed to load attempts.</p>";

        console.error(error);
    }
}


// ===============================
// ESCAPE HTML
// ===============================

function escapeHtml(value) {

    if (
        value === null ||
        value === undefined
    ) {
        return "";
    }

    return String(value)
        .replace(/&/g, "&amp;")
        .replace(/</g, "&lt;")
        .replace(/>/g, "&gt;")
        .replace(/"/g, "&quot;")
        .replace(/'/g, "&#039;");
}


// ===============================
// FORMAT DATE
// ===============================

function formatDate(date) {

    if (!date) {
        return "";
    }

    return new Date(date).toLocaleString();
}


// ===============================
// INITIAL PAGE
// ===============================

showSection("dashboard");